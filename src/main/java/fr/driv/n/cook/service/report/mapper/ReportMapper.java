package fr.driv.n.cook.service.report.mapper;

import fr.driv.n.cook.config.ApplicationMapperConfig;
import fr.driv.n.cook.presentation.report.dto.Report;
import fr.driv.n.cook.presentation.report.dto.ReportRequest;
import fr.driv.n.cook.repository.report.entity.ReportEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = ApplicationMapperConfig.class)
public interface ReportMapper {

    Report toDto(ReportEntity entity);
}

