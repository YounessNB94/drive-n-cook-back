package fr.driv.n.cook.service.revenue.mapper;

import fr.driv.n.cook.config.ApplicationMapperConfig;
import fr.driv.n.cook.presentation.revenue.dto.RevenuePoint;
import fr.driv.n.cook.repository.revenue.entity.RevenuePointEntity;
import org.mapstruct.Mapper;

@Mapper(config = ApplicationMapperConfig.class)
public interface RevenuePointMapper {

    RevenuePoint toDto(RevenuePointEntity entity);
}

