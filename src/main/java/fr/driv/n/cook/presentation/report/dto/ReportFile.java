package fr.driv.n.cook.presentation.report.dto;

public record ReportFile(
        String fileName,
        String contentType,
        byte[] content
) {
}
