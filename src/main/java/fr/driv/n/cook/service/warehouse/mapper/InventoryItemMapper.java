package fr.driv.n.cook.service.warehouse.mapper;

import fr.driv.n.cook.config.ApplicationMapperConfig;
import fr.driv.n.cook.presentation.warehouse.dto.InventoryItem;
import fr.driv.n.cook.presentation.warehouse.dto.InventoryItemPatch;
import fr.driv.n.cook.repository.warehouse.entity.InventoryItemEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = ApplicationMapperConfig.class)
public interface InventoryItemMapper {

    @Mapping(target = "warehouseId", source = "warehouse.id")
    InventoryItem toDto(InventoryItemEntity entity);

    @Mapping(target = "warehouse", ignore = true)
    InventoryItemEntity toEntity(InventoryItem dto);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "availableQuantity", source = "availableQuantity")
    void updateEntityFromPatch(InventoryItemPatch patch, @MappingTarget InventoryItemEntity entity);
}
