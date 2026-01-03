package fr.driv.n.cook.service.truck.mapper;

import fr.driv.n.cook.config.ApplicationMapperConfig;
import fr.driv.n.cook.presentation.truck.dto.MaintenanceRecord;
import fr.driv.n.cook.repository.truck.entity.MaintenanceRecordEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = ApplicationMapperConfig.class)
public interface MaintenanceRecordMapper {

    @Mapping(target = "truckId", source = "truck.id")
    MaintenanceRecord toDto(MaintenanceRecordEntity entity);
}
