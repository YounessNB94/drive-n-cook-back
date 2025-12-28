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

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "franchisee", ignore = true)
    @Mapping(target = "status", expression = "java(fr.driv.n.cook.shared.ReportStatus.PENDING)")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "filePath", ignore = true)
    @Mapping(target = "type", source = "type")
    @Mapping(target = "from", source = "from")
    @Mapping(target = "to", source = "to")
    ReportEntity toEntity(ReportRequest request);
}

