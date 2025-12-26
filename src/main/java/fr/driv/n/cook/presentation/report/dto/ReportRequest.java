package fr.driv.n.cook.presentation.report.dto;

import fr.driv.n.cook.presentation.shared.dto.ReportType;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ReportRequest(
        @NotNull ReportType type,
        @NotNull LocalDate from,
        @NotNull @FutureOrPresent LocalDate to
) {
}
