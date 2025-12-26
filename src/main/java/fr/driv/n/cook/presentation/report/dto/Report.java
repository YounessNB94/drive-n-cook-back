package fr.driv.n.cook.presentation.report.dto;

import fr.driv.n.cook.presentation.shared.dto.ReportStatus;
import fr.driv.n.cook.presentation.shared.dto.ReportType;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record Report(
        Long id,
        ReportType type,
        LocalDate from,
        LocalDate to,
        ReportStatus status,
        LocalDateTime createdAt
) {
}
