package fr.driv.n.cook.presentation.report.dto;

import fr.driv.n.cook.shared.ReportType;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ReportRequest(
        @NotNull ReportType type,
        @NotNull LocalDateTime from,
        @NotNull LocalDateTime to,
        Long franchiseeId
) {
}
