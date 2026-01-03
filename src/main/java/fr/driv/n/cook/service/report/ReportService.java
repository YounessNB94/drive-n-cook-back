package fr.driv.n.cook.service.report;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import fr.driv.n.cook.presentation.report.dto.Report;
import fr.driv.n.cook.presentation.report.dto.ReportFile;
import fr.driv.n.cook.presentation.report.dto.ReportRequest;
import fr.driv.n.cook.repository.franchisee.FranchiseeRepository;
import fr.driv.n.cook.repository.franchisee.entity.FranchiseeEntity;
import fr.driv.n.cook.repository.report.ReportRepository;
import fr.driv.n.cook.repository.report.entity.ReportEntity;
import fr.driv.n.cook.service.report.mapper.ReportMapper;
import fr.driv.n.cook.shared.ReportStatus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.awt.Color;
import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class ReportService {

    @Inject
    ReportRepository reportRepository;

    @Inject
    FranchiseeRepository franchiseeRepository;

    @Inject
    ReportMapper mapper;

    @Transactional
    public ReportFile generateReport(Long franchiseeId, ReportRequest request) {
        FranchiseeEntity franchisee = fetchFranchisee(franchiseeId);
        ReportEntity entity = mapper.toEntity(request);
        entity.setFranchisee(franchisee);
        entity.setStatus(ReportStatus.READY);
        reportRepository.persist(entity);
        byte[] pdfBytes = generatePdf(entity);
        return new ReportFile("report-" + entity.getId() + ".pdf", "application/pdf", pdfBytes);
    }

    public Report getReport(Long reportId) {
        return mapper.toDto(fetchReport(reportId));
    }

    private byte[] generatePdf(ReportEntity entity) {
        try (var buffer = new java.io.ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4, 36, 36, 54, 36);
            PdfWriter.getInstance(document, buffer);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA, 11);
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.WHITE);
            Font valueFont = FontFactory.getFont(FontFactory.HELVETICA, 10);

            document.add(new Paragraph("Rapport Driv'n Cook", titleFont));
            document.add(new Paragraph("Nom compagnie : " + entity.getFranchisee().getCompanyName(), subtitleFont));
            document.add(new Paragraph("Généré le : " + LocalDateTime.now(), subtitleFont));
            document.add(Chunk.NEWLINE);

            PdfPTable metaTable = new PdfPTable(new float[]{2f, 4f});
            metaTable.setWidthPercentage(100);
            addMetaRow(metaTable, "Rapport", entity.getType().name(), headerFont, valueFont);
            addMetaRow(metaTable, "Période", entity.getFrom() + " → " + entity.getTo(), headerFont, valueFont);
            addMetaRow(metaTable, "Statut", entity.getStatus().name(), headerFont, valueFont);
            addMetaRow(metaTable, "Créé le", entity.getCreatedAt() != null ? entity.getCreatedAt().toString() : "—", headerFont, valueFont);
            document.add(metaTable);

            document.add(Chunk.NEWLINE);
            document.add(new Paragraph("Synthèse (extrait)", subtitleFont));
            document.add(Chunk.NEWLINE);

            PdfPTable summary = new PdfPTable(3);
            summary.setWidthPercentage(100);
            summary.setWidths(new float[]{2f, 1f, 3f});
            addHeaderCell(summary, "Indicateur", headerFont);
            addHeaderCell(summary, "Valeur", headerFont);
            addHeaderCell(summary, "Commentaire", headerFont);

            addSummaryRow(summary, "Commandes traitées", "—", "Données calculées côté base prochainement.", valueFont);
            addSummaryRow(summary, "CA total", "—", "Aligné sur `RevenuePointEntity`.", valueFont);
            addSummaryRow(summary, "Incidents", "—", "Pour les rapports TRUCK_STATUS.", valueFont);
            document.add(summary);

            document.close();
            return buffer.toByteArray();
        } catch (DocumentException | java.io.IOException e) {
            throw new IllegalStateException("Impossible de générer le PDF", e);
        }
    }

    private void addMetaRow(PdfPTable table, String label, String value, Font headerFont, Font valueFont) {
        PdfPCell keyCell = new PdfPCell(new Phrase(label, headerFont));
        keyCell.setBackgroundColor(new Color(38, 70, 83));
        keyCell.setPadding(6);
        table.addCell(keyCell);

        PdfPCell valueCell = new PdfPCell(new Phrase(value != null ? value : "—", valueFont));
        valueCell.setPadding(6);
        table.addCell(valueCell);
    }

    private void addHeaderCell(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(new Color(38, 70, 83));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(6);
        table.addCell(cell);
    }

    private void addSummaryRow(PdfPTable table, String indicator, String value, String comment, Font font) {
        PdfPCell indicatorCell = new PdfPCell(new Phrase(indicator, font));
        indicatorCell.setPadding(6);
        table.addCell(indicatorCell);

        PdfPCell valueCell = new PdfPCell(new Phrase(value, font));
        valueCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        valueCell.setPadding(6);
        table.addCell(valueCell);

        PdfPCell commentCell = new PdfPCell(new Phrase(comment, font));
        commentCell.setPadding(6);
        table.addCell(commentCell);
    }

    private ReportEntity fetchReport(Long reportId) {
        return reportRepository.findByIdOptional(reportId)
                .orElseThrow(() -> new NotFoundException("Rapport introuvable"));
    }

    private FranchiseeEntity fetchFranchisee(Long franchiseeId) {
        return franchiseeRepository.findByIdOptional(franchiseeId)
                .orElseThrow(() -> new NotFoundException("Franchisé introuvable"));
    }
}
