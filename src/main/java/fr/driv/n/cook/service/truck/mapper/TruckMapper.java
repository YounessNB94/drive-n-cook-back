package fr.driv.n.cook.service.truck.mapper;

import fr.driv.n.cook.config.ApplicationMapperConfig;
import fr.driv.n.cook.presentation.truck.dto.Truck;
import fr.driv.n.cook.repository.truck.entity.TruckEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = ApplicationMapperConfig.class)
public interface TruckMapper {

    @Mapping(target = "currentWarehouseId", source = "currentWarehouse.id")
    @Mapping(target = "assignedFranchiseeId", source = "franchisee.id")
    Truck toDto(TruckEntity entity);
}
