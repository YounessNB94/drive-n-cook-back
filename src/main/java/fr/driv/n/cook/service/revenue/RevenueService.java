package fr.driv.n.cook.service.revenue;

import fr.driv.n.cook.presentation.revenue.dto.RevenuePoint;
import fr.driv.n.cook.repository.revenue.RevenuePointRepository;
import fr.driv.n.cook.repository.revenue.entity.RevenuePointEntity;
import fr.driv.n.cook.service.revenue.mapper.RevenuePointMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class RevenueService {

    @Inject
    RevenuePointRepository revenuePointRepository;

    @Inject
    RevenuePointMapper mapper;

    public List<RevenuePoint> listRevenuePoints(Long franchiseeId, LocalDate from, LocalDate to) {
        LocalDate effectiveFrom = from != null ? from : LocalDate.now().minusDays(7);
        LocalDate effectiveTo = to != null ? to : LocalDate.now();
        List<RevenuePointEntity> entities = revenuePointRepository.listByFranchiseeAndPeriod(franchiseeId, effectiveFrom, effectiveTo);
        if (entities.isEmpty()) {
            return stubSeries(effectiveFrom, effectiveTo);
        }
        return entities.stream().map(mapper::toDto).toList();
    }

    private List<RevenuePoint> stubSeries(LocalDate from, LocalDate to) {
        BigDecimal amount = new BigDecimal("1000.00");
        return List.of(
                new RevenuePoint(from, from.plusDays(3), amount),
                new RevenuePoint(to.minusDays(3), to, amount.add(new BigDecimal("150.50")))
        );
    }
}

