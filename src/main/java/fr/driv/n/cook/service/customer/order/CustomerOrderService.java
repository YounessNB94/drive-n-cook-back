package fr.driv.n.cook.service.customer.order;

import fr.driv.n.cook.presentation.customer.order.dto.CustomerOrder;
import fr.driv.n.cook.presentation.customer.order.dto.CustomerOrderItem;
import fr.driv.n.cook.presentation.customer.order.dto.CustomerOrderPatch;
import fr.driv.n.cook.repository.customer.order.CustomerOrderItemRepository;
import fr.driv.n.cook.repository.customer.order.CustomerOrderRepository;
import fr.driv.n.cook.repository.customer.order.entity.CustomerOrderEntity;
import fr.driv.n.cook.repository.customer.order.entity.CustomerOrderItemEntity;
import fr.driv.n.cook.repository.franchisee.FranchiseeRepository;
import fr.driv.n.cook.repository.franchisee.entity.FranchiseeEntity;
import fr.driv.n.cook.repository.loyalty.card.LoyaltyCardRepository;
import fr.driv.n.cook.repository.loyalty.card.entity.LoyaltyCardEntity;
import fr.driv.n.cook.repository.menu.MenuItemRepository;
import fr.driv.n.cook.repository.menu.entity.MenuItemEntity;
import fr.driv.n.cook.repository.sale.SaleRepository;
import fr.driv.n.cook.repository.sale.entity.SaleEntity;
import fr.driv.n.cook.service.customer.order.mapper.CustomerOrderMapper;
import fr.driv.n.cook.shared.CustomerOrderStatus;
import fr.driv.n.cook.shared.CustomerOrderType;
import fr.driv.n.cook.shared.PaymentMethod;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class CustomerOrderService {

    @Inject
    CustomerOrderRepository orderRepository;

    @Inject
    CustomerOrderItemRepository orderItemRepository;

    @Inject
    FranchiseeRepository franchiseeRepository;

    @Inject
    MenuItemRepository menuItemRepository;

    @Inject
    LoyaltyCardRepository loyaltyCardRepository;

    @Inject
    SaleRepository saleRepository;

    @Inject
    CustomerOrderMapper mapper;

    @Transactional
    public CustomerOrder createOrder(Long franchiseeId, CustomerOrder request) {
        FranchiseeEntity franchisee = fetchFranchisee(franchiseeId);
        CustomerOrderEntity entity = new CustomerOrderEntity();
        entity.setFranchisee(franchisee);
        entity.setType(request.type());
        entity.setStatus(CustomerOrderStatus.CREATED);
        entity.setPaid(false);
        if (request.loyaltyCardId() != null) {
            entity.setLoyaltyCard(fetchLoyaltyCard(request.loyaltyCardId()));
        }
        orderRepository.persist(entity);
        return mapper.toDto(entity);
    }

    public CustomerOrder getOrder(Long orderId) {
        return mapper.toDto(fetchOrder(orderId));
    }

    public List<CustomerOrderItem> listItems(Long orderId) {
        return orderItemRepository.listByOrder(orderId).stream()
                .map(mapper::toDto)
                .toList();
    }

    @Transactional
    public CustomerOrderItem addItem(Long orderId, CustomerOrderItem item) {
        CustomerOrderEntity order = fetchOrder(orderId);
        ensureOrderOpen(order);
        MenuItemEntity menuItem = fetchMenuItem(item.menuItemId());

        CustomerOrderItemEntity entity = new CustomerOrderItemEntity();
        entity.setCustomerOrder(order);
        entity.setMenuItem(menuItem);
        entity.setQuantity(item.quantity());
        entity.setLineCashTotal(menuItem.getPriceCash().multiply(BigDecimal.valueOf(item.quantity())));
        if (menuItem.getPointsPrice() != null) {
            entity.setLinePointsTotal(menuItem.getPointsPrice() * item.quantity());
        }

        orderItemRepository.persist(entity);
        recalculateTotals(order);
        return mapper.toDto(entity);
    }

    @Transactional
    public CustomerOrder patchOrder(Long orderId, CustomerOrderPatch patch) {
        CustomerOrderEntity entity = fetchOrder(orderId);
        ensureOrderMutable(entity);
        mapper.updateEntityFromPatch(patch, entity);
        validatePaymentRules(entity);
        if (entity.isPaid()) {
            recordSales(entity);
        }
        return mapper.toDto(entity);
    }

    private void validatePaymentRules(CustomerOrderEntity entity) {
        if (entity.isPaid() && entity.getPaymentMethod() == null) {
            throw new BadRequestException("paymentMethod requis lorsque paid=true");
        }
        if (entity.getPaymentMethod() == PaymentMethod.POINTS) {
            if (entity.getLoyaltyCard() == null) {
                throw new BadRequestException("Paiement par points impossible sans carte de fidélité");
            }
            if (entity.getTotalPoints() == null || entity.getLoyaltyCard().getPointsBalance() < entity.getTotalPoints()) {
                throw new BadRequestException("Solde de points insuffisant");
            }
            entity.getLoyaltyCard().setPointsBalance(entity.getLoyaltyCard().getPointsBalance() - entity.getTotalPoints());
        }
        if (entity.getPaymentMethod() == PaymentMethod.CASH && (entity.getTotalCash() == null || entity.getTotalCash().compareTo(BigDecimal.ZERO) <= 0)) {
            throw new BadRequestException("Montant cash invalide");
        }
    }

    private void recalculateTotals(CustomerOrderEntity order) {
        BigDecimal totalCash = order.getItems().stream()
                .map(CustomerOrderItemEntity::getLineCashTotal)
                .filter(sum -> sum != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        Integer totalPoints = order.getItems().stream()
                .map(CustomerOrderItemEntity::getLinePointsTotal)
                .filter(sum -> sum != null)
                .reduce(0, Integer::sum);
        order.setTotalCash(totalCash);
        order.setTotalPoints(totalPoints == 0 ? null : totalPoints);
    }

    private void recordSales(CustomerOrderEntity order) {
        order.getItems().forEach(item -> {
            SaleEntity sale = new SaleEntity();
            sale.setCustomerOrder(order);
            sale.setMenuItem(item.getMenuItem());
            sale.setDate(LocalDateTime.now());
            sale.setQuantity(item.getQuantity());
            sale.setTotalAmount(item.getLineCashTotal());
            saleRepository.persist(sale);
        });
        order.setStatus(CustomerOrderStatus.COMPLETED);
    }

    private void ensureOrderOpen(CustomerOrderEntity order) {
        if (order.getStatus() == CustomerOrderStatus.COMPLETED || order.getStatus() == CustomerOrderStatus.CANCELLED) {
            throw new BadRequestException("Commande clôturée");
        }
    }

    private void ensureOrderMutable(CustomerOrderEntity order) {
        if (order.isPaid()) {
            throw new BadRequestException("Commande déjà réglée");
        }
    }

    private FranchiseeEntity fetchFranchisee(Long franchiseeId) {
        return franchiseeRepository.findByIdOptional(franchiseeId)
                .orElseThrow(() -> new NotFoundException("Franchisé introuvable"));
    }

    private CustomerOrderEntity fetchOrder(Long orderId) {
        return orderRepository.findByIdOptional(orderId)
                .orElseThrow(() -> new NotFoundException("Commande client introuvable"));
    }

    private MenuItemEntity fetchMenuItem(Long menuItemId) {
        return menuItemRepository.findByIdOptional(menuItemId)
                .orElseThrow(() -> new NotFoundException("Item de menu introuvable"));
    }

    private LoyaltyCardEntity fetchLoyaltyCard(Long cardId) {
        return loyaltyCardRepository.findByIdOptional(cardId)
                .orElseThrow(() -> new NotFoundException("Carte de fidélité introuvable"));
    }
}
