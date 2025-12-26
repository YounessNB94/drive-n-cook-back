package fr.driv.n.cook.presentation.report.dto;

import fr.driv.n.cook.shared.ReportStatus;
import fr.driv.n.cook.shared.ReportType;

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
