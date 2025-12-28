package fr.driv.n.cook.config;

import org.mapstruct.*;

@MapperConfig(
        componentModel = MappingConstants.ComponentModel.JAKARTA_CDI,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface ApplicationMapperConfig {
}
