package fr.driv.n.cook.service.truck.mapper;

import fr.driv.n.cook.config.ApplicationMapperConfig;
import fr.driv.n.cook.presentation.incident.dto.Incident;
import fr.driv.n.cook.presentation.truck.dto.MaintenanceRecord;
import fr.driv.n.cook.presentation.truck.dto.Truck;
import fr.driv.n.cook.repository.incident.entity.IncidentEntity;
import fr.driv.n.cook.repository.truck.entity.MaintenanceRecordEntity;
import fr.driv.n.cook.repository.truck.entity.TruckEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = ApplicationMapperConfig.class)
public interface TruckMapper {

    @Mapping(target = "currentWarehouseId", source = "currentWarehouse.id")
    @Mapping(target = "assignedFranchiseeId", source = "franchisee.id")
    Truck toDto(TruckEntity entity);

    @Mapping(target = "truckId", source = "truck.id")
    Incident toDto(IncidentEntity entity);

    @Mapping(target = "truckId", source = "truck.id")
    MaintenanceRecord toDto(MaintenanceRecordEntity entity);
}
