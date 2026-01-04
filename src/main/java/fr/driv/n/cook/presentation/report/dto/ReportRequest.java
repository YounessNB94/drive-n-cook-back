package fr.driv.n.cook.presentation.report.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ReportRequest(
        @NotNull LocalDate from,
        @NotNull LocalDate to,
        Long franchiseeId
) {
}
