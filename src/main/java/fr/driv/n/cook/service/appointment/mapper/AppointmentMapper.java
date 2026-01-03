package fr.driv.n.cook.service.appointment.mapper;

import fr.driv.n.cook.config.ApplicationMapperConfig;
import fr.driv.n.cook.presentation.appointment.dto.Appointment;
import fr.driv.n.cook.presentation.appointment.dto.AppointmentPatch;
import fr.driv.n.cook.repository.appointment.entity.AppointmentEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = ApplicationMapperConfig.class)
public interface AppointmentMapper {

    @Mapping(target = "warehouseId", source = "warehouse.id")
    @Mapping(target = "franchiseeId", source = "franchisee.id")
    @Mapping(target = "supplyOrderId", source = "supplyOrder.id")
    @Mapping(target = "truckId", source = "truck.id")
    Appointment toDto(AppointmentEntity entity);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "datetime", source = "datetime")
    @Mapping(target = "status", source = "status")
    void updateEntityFromPatch(AppointmentPatch patch, @MappingTarget AppointmentEntity entity);
}
