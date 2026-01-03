package fr.driv.n.cook.service.incident.mapper;

import fr.driv.n.cook.config.ApplicationMapperConfig;
import fr.driv.n.cook.presentation.incident.dto.Incident;
import fr.driv.n.cook.repository.incident.entity.IncidentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = ApplicationMapperConfig.class)
public interface IncidentMapper {

    @Mapping(target = "truckId", source = "truck.id")
    Incident toDto(IncidentEntity entity);
}
