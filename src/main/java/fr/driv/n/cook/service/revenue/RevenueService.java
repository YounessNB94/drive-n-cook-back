package fr.driv.n.cook.service.revenue;

import fr.driv.n.cook.presentation.revenue.dto.RevenuePoint;
import fr.driv.n.cook.repository.sale.SaleRepository;
import fr.driv.n.cook.repository.sale.entity.SaleEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class RevenueService {

    @Inject
    SaleRepository saleRepository;

    public List<RevenuePoint> listRevenuePoints(Long franchiseeId, LocalDate from, LocalDate to) {
        if (franchiseeId == null) {
            throw new IllegalArgumentException("franchiseeId requis pour les appels admin");
        }
        LocalDate effectiveFrom = from != null ? from : LocalDate.now().minusDays(7);
        LocalDate effectiveTo = to != null ? to : LocalDate.now();
        LocalDateTime fromDateTime = effectiveFrom.atStartOfDay();
        LocalDateTime toDateTime = effectiveTo.atTime(LocalTime.MAX);
        List<SaleEntity> sales = saleRepository.list("franchisee.id = ?1 and date between ?2 and ?3",
                franchiseeId, fromDateTime, toDateTime);
        Map<LocalDate, BigDecimal> perDay = new LinkedHashMap<>();
        LocalDate cursor = effectiveFrom;
        while (!cursor.isAfter(effectiveTo)) {
            perDay.put(cursor, BigDecimal.ZERO);
            cursor = cursor.plusDays(1);
        }
        for (SaleEntity sale : sales) {
            LocalDate day = sale.getDate().toLocalDate();
            perDay.computeIfPresent(day, (d, amount) -> amount.add(sale.getTotalAmount()));
        }
        return perDay.entrySet().stream()
                .map(entry -> new RevenuePoint(entry.getKey(), entry.getKey(), entry.getValue()))
                .toList();
    }
}
