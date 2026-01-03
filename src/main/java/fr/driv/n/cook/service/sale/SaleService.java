package fr.driv.n.cook.service.sale;

import fr.driv.n.cook.presentation.sale.dto.Sale;
import fr.driv.n.cook.repository.sale.SaleRepository;
import fr.driv.n.cook.repository.sale.entity.SaleEntity;
import fr.driv.n.cook.service.sale.mapper.SaleMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class SaleService {

    @Inject
    SaleRepository saleRepository;

    @Inject
    SaleMapper mapper;

    public List<Sale> listSales(Long franchiseeId, LocalDateTime from, LocalDateTime to, Long menuItemId) {
        LocalDateTime effectiveFrom = from != null ? from : LocalDateTime.now().minusDays(7);
        LocalDateTime effectiveTo = to != null ? to : LocalDateTime.now();
        List<SaleEntity> entities;
        if (franchiseeId != null && menuItemId != null) {
            entities = saleRepository.list("customerOrder.franchisee.id = ?1 and menuItem.id = ?2 and date between ?3 and ?4",
                    franchiseeId, menuItemId, effectiveFrom, effectiveTo);
        } else if (franchiseeId != null) {
            entities = saleRepository.list("customerOrder.franchisee.id = ?1 and date between ?2 and ?3",
                    franchiseeId, effectiveFrom, effectiveTo);
        } else if (menuItemId != null) {
            entities = saleRepository.list("menuItem.id = ?1 and date between ?2 and ?3",
                    menuItemId, effectiveFrom, effectiveTo);
        } else {
            entities = saleRepository.list("date between ?1 and ?2", effectiveFrom, effectiveTo);
        }
        return entities.stream().map(mapper::toDto).toList();
    }
}
