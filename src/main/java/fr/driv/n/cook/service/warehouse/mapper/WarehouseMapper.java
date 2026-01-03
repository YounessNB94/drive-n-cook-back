package fr.driv.n.cook.service.warehouse.mapper;

import fr.driv.n.cook.config.ApplicationMapperConfig;
import fr.driv.n.cook.presentation.warehouse.dto.InventoryItem;
import fr.driv.n.cook.presentation.warehouse.dto.Warehouse;
import fr.driv.n.cook.repository.warehouse.entity.InventoryItemEntity;
import fr.driv.n.cook.repository.warehouse.entity.WarehouseEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = ApplicationMapperConfig.class)
public interface WarehouseMapper {

    Warehouse toDto(WarehouseEntity entity);

    @Mapping(target = "warehouseId", source = "warehouse.id")
    InventoryItem toDto(InventoryItemEntity entity);
}
