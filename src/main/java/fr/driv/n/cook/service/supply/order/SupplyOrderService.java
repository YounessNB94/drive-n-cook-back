package fr.driv.n.cook.service.supply.order;

import fr.driv.n.cook.presentation.supply.order.dto.SupplyOrder;
import fr.driv.n.cook.presentation.supply.order.dto.SupplyOrderItem;
import fr.driv.n.cook.presentation.supply.order.dto.SupplyOrderItemPatch;
import fr.driv.n.cook.presentation.supply.order.dto.SupplyOrderPatch;
import fr.driv.n.cook.repository.franchisee.FranchiseeRepository;
import fr.driv.n.cook.repository.franchisee.entity.FranchiseeEntity;
import fr.driv.n.cook.repository.supply.order.SupplyOrderItemRepository;
import fr.driv.n.cook.repository.supply.order.SupplyOrderRepository;
import fr.driv.n.cook.repository.supply.order.entity.SupplyOrderEntity;
import fr.driv.n.cook.repository.supply.order.entity.SupplyOrderItemEntity;
import fr.driv.n.cook.repository.warehouse.InventoryItemRepository;
import fr.driv.n.cook.repository.warehouse.WarehouseRepository;
import fr.driv.n.cook.repository.warehouse.entity.InventoryItemEntity;
import fr.driv.n.cook.repository.warehouse.entity.WarehouseEntity;
import fr.driv.n.cook.service.supply.order.mapper.SupplyOrderMapper;
import fr.driv.n.cook.shared.SupplyOrderStatus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.NotFoundException;

import java.util.List;

@ApplicationScoped
public class SupplyOrderService {

    @Inject
    SupplyOrderRepository supplyOrderRepository;

    @Inject
    SupplyOrderItemRepository supplyOrderItemRepository;

    @Inject
    WarehouseRepository warehouseRepository;

    @Inject
    InventoryItemRepository inventoryItemRepository;

    @Inject
    FranchiseeRepository franchiseeRepository;

    @Inject
    SupplyOrderMapper mapper;

    @Transactional
    public SupplyOrder createDraft(Long franchiseeId) {
        FranchiseeEntity franchisee = fetchFranchisee(franchiseeId);
        SupplyOrderEntity entity = new SupplyOrderEntity();
        entity.setFranchisee(franchisee);
        entity.setStatus(SupplyOrderStatus.DRAFT);
        entity.setPaid(false);
        supplyOrderRepository.persist(entity);
        return mapper.toDto(entity);
    }

    public List<SupplyOrder> listForFranchisee(Long franchiseeId) {
        return supplyOrderRepository.listByFranchisee(franchiseeId).stream()
                .map(mapper::toDto)
                .toList();
    }

    @Transactional
    public SupplyOrder patchOrder(Long orderId, SupplyOrderPatch patch) {
        SupplyOrderEntity entity = fetchOrder(orderId);
        if (patch.pickupWarehouseId() != null) {
            WarehouseEntity warehouse = fetchWarehouse(patch.pickupWarehouseId());
            entity.setPickupWarehouse(warehouse);
        }
        if (patch.status() != null) {
            if (patch.status() == SupplyOrderStatus.READY) {
                throw new BadRequestException("Seul l'admin peut passer une commande en READY");
            }
            ensureValidTransition(entity.getStatus(), patch.status());
            entity.setStatus(patch.status());
        }
        if (patch.paid() != null) {
            entity.setPaid(patch.paid());
        }
        if (patch.paymentMethod() != null) {
            entity.setPaymentMethod(patch.paymentMethod());
        }
        if (patch.paymentRef() != null) {
            entity.setPaymentRef(patch.paymentRef());
        }
        if (entity.isPaid() && entity.getPaymentMethod() == null) {
            throw new BadRequestException("paymentMethod obligatoire lorsque paid=true");
        }
        return mapper.toDto(entity);
    }

    @Transactional
    public SupplyOrderItem addItem(Long orderId, SupplyOrderItem item) {
        SupplyOrderEntity order = fetchOrder(orderId);
        ensureOrderAllowsItemMutation(order.getStatus());
        InventoryItemEntity inventoryItem = fetchInventoryItem(item.inventoryItemId());
        validateQuantity(item.quantity());

        SupplyOrderItemEntity entity = new SupplyOrderItemEntity();
        entity.setSupplyOrder(order);
        entity.setInventoryItem(inventoryItem);
        entity.setQuantity(item.quantity());

        supplyOrderItemRepository.persist(entity);
        return mapper.toDto(entity);
    }

    @Transactional
    public SupplyOrderItem patchItem(Long itemId, SupplyOrderItemPatch patch) {
        SupplyOrderItemEntity entity = fetchOrderItem(itemId);
        ensureOrderAllowsItemMutation(entity.getSupplyOrder().getStatus());
        validateQuantity(patch.quantity());
        entity.setQuantity(patch.quantity());
        return mapper.toDto(entity);
    }

    public List<SupplyOrder> listOrdersForAdmin(SupplyOrderStatus status, Long warehouseId, Long franchiseeId, Boolean paid) {
        return supplyOrderRepository.listByFilters(status, warehouseId, franchiseeId, paid).stream()
                .map(mapper::toDto)
                .toList();
    }

    public SupplyOrder getOrder(Long orderId) {
        return mapper.toDto(fetchOrder(orderId));
    }

    public SupplyOrder getOrderForFranchisee(Long orderId, Long franchiseeId) {
        return mapper.toDto(fetchOrderForFranchisee(orderId, franchiseeId));
    }

    public List<SupplyOrderItem> listItems(Long orderId) {
        fetchOrder(orderId);
        return supplyOrderItemRepository.listByOrder(orderId).stream()
                .map(mapper::toDto)
                .toList();
    }

    public void assertOrderOwnedByFranchisee(Long orderId, Long franchiseeId) {
        fetchOrderForFranchisee(orderId, franchiseeId);
    }

    private SupplyOrderEntity fetchOrderForFranchisee(Long orderId, Long franchiseeId) {
        SupplyOrderEntity entity = fetchOrder(orderId);
        if (!entity.getFranchisee().getId().equals(franchiseeId)) {
            throw new ForbiddenException("Commande inaccessible");
        }
        return entity;
    }

    @Transactional
    public SupplyOrder markReady(Long orderId) {
        SupplyOrderEntity entity = fetchOrder(orderId);
        ensureValidTransition(entity.getStatus(), SupplyOrderStatus.READY);
        entity.setStatus(SupplyOrderStatus.READY);
        return mapper.toDto(entity);
    }

    private void ensureValidTransition(SupplyOrderStatus current, SupplyOrderStatus next) {
        if (current == next) {
            return;
        }
        if (current == SupplyOrderStatus.DRAFT && next == SupplyOrderStatus.CONFIRMED) {
            return;
        }
        if (current == SupplyOrderStatus.CONFIRMED && (next == SupplyOrderStatus.READY || next == SupplyOrderStatus.COLLECTED)) {
            return;
        }
        if (current == SupplyOrderStatus.READY && next == SupplyOrderStatus.COLLECTED) {
            return;
        }
        throw new BadRequestException("Transition de statut invalide");
    }

    private void ensureOrderAllowsItemMutation(SupplyOrderStatus status) {
        if (status == SupplyOrderStatus.READY || status == SupplyOrderStatus.COLLECTED) {
            throw new BadRequestException("Impossible de modifier une commande prête ou collectée");
        }
    }

    private void validateQuantity(Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new BadRequestException("La quantité doit être positive");
        }
    }

    private FranchiseeEntity fetchFranchisee(Long franchiseeId) {
        return franchiseeRepository.findByIdOptional(franchiseeId)
                .orElseThrow(() -> new NotFoundException("Franchisé introuvable"));
    }

    private SupplyOrderEntity fetchOrder(Long orderId) {
        return supplyOrderRepository.findByIdOptional(orderId)
                .orElseThrow(() -> new NotFoundException("Commande introuvable"));
    }

    private SupplyOrderItemEntity fetchOrderItem(Long itemId) {
        return supplyOrderItemRepository.findByIdOptional(itemId)
                .orElseThrow(() -> new NotFoundException("Article introuvable"));
    }

    private WarehouseEntity fetchWarehouse(Long warehouseId) {
        return warehouseRepository.findByIdOptional(warehouseId)
                .orElseThrow(() -> new NotFoundException("Entrepôt introuvable"));
    }

    private InventoryItemEntity fetchInventoryItem(Long inventoryItemId) {
        return inventoryItemRepository.findByIdOptional(inventoryItemId)
                .orElseThrow(() -> new NotFoundException("Article d'inventaire introuvable"));
    }
}
