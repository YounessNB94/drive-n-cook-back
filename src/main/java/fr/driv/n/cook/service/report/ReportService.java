package fr.driv.n.cook.service.report;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import fr.driv.n.cook.presentation.report.dto.ReportFile;
import fr.driv.n.cook.presentation.report.dto.ReportRequest;
import fr.driv.n.cook.presentation.sale.dto.Sale;
import fr.driv.n.cook.repository.franchisee.FranchiseeRepository;
import fr.driv.n.cook.repository.franchisee.entity.FranchiseeEntity;
import fr.driv.n.cook.repository.sale.SaleRepository;
import fr.driv.n.cook.repository.sale.entity.SaleEntity;
import fr.driv.n.cook.service.sale.SaleService;
import fr.driv.n.cook.service.sale.mapper.SaleMapper;
import fr.driv.n.cook.shared.ReportType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class ReportService {

    @Inject
    SaleRepository saleRepository;

    @Inject
    FranchiseeRepository franchiseeRepository;

    @Transactional
    public ReportFile generateReport(Long franchiseeId, ReportRequest request) {
        FranchiseeEntity franchisee = fetchFranchisee(franchiseeId);
        LocalDate fromDate = request.from() != null ? request.from() : LocalDate.now().minusDays(7);
        LocalDate toDate = request.to() != null ? request.to() : LocalDate.now();
        List<SaleEntity> sales = saleRepository.listSales(franchiseeId, fromDate.atStartOfDay(), toDate.atTime(23, 59, 59));
        byte[] pdfBytes = generatePdf(franchisee, sales, fromDate, toDate);
        return new ReportFile("report-" + LocalDateTime.now() + ".pdf", "application/pdf", pdfBytes);
    }

    private byte[] generatePdf(FranchiseeEntity franchisee, List<SaleEntity> sales, LocalDate from, LocalDate to) {
        try (var buffer = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4, 36, 36, 54, 36);
            PdfWriter.getInstance(document, buffer);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA, 11);
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.WHITE);
            Font valueFont = FontFactory.getFont(FontFactory.HELVETICA, 10);

            document.add(new Paragraph("Rapport Driv'n Cook", titleFont));
            document.add(new Paragraph("Nom compagnie : " + franchisee.getCompanyName(), subtitleFont));
            document.add(new Paragraph("Généré le : " + LocalDateTime.now(), subtitleFont));
            document.add(Chunk.NEWLINE);

            PdfPTable metaTable = new PdfPTable(new float[]{2f, 4f});
            metaTable.setWidthPercentage(100);
            addMetaRow(metaTable, "Rapport", ReportType.SALES_STATS.name(), headerFont, valueFont);
            addMetaRow(metaTable, "Période", from + " → " + to, headerFont, valueFont);
            addMetaRow(metaTable, "Créé le", LocalDateTime.now().toString(), headerFont, valueFont);
            document.add(metaTable);

            document.add(Chunk.NEWLINE);

            PdfPTable salesTable = new PdfPTable(4);
            salesTable.setWidthPercentage(100);
            salesTable.setWidths(new float[]{4f, 3f, 2f, 2f});
            addHeaderCell(salesTable, "Produit", headerFont);
            addHeaderCell(salesTable, "Date de vente", headerFont);
            addHeaderCell(salesTable, "Prix", headerFont);
            addHeaderCell(salesTable, "Quantite", headerFont);

            int totalAmount = 0;
            for (SaleEntity sale : sales) {
                addSummaryRow(salesTable, sale.getMenuItem().getName(), sale.getDate().toString(), sale.getTotalAmount().toString(), sale.getQuantity().toString(), valueFont);
                totalAmount += sale.getTotalAmount().intValue();
            }
            addTotalAmountRow(salesTable, totalAmount, valueFont);

            document.add(salesTable);
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

    private void addSummaryRow(PdfPTable table, String item, String date, String totalAmount, String quantity, Font font) {
        PdfPCell itemCell = new PdfPCell(new Phrase(item, font));
        itemCell.setPadding(6);
        table.addCell(itemCell);

        PdfPCell dateCell = new PdfPCell(new Phrase(date, font));
        dateCell.setPadding(6);
        table.addCell(dateCell);

        PdfPCell totalAmountCell = new PdfPCell(new Phrase(totalAmount, font));
        totalAmountCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        totalAmountCell.setPadding(6);
        table.addCell(totalAmountCell);

        PdfPCell quantityCell = new PdfPCell(new Phrase(quantity, font));
        quantityCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        quantityCell.setPadding(6);
        table.addCell(quantityCell);
    }

    private void addTotalAmountRow(PdfPTable table, int totalAmount, Font font) {
        PdfPCell totalLabelCell = new PdfPCell(new Phrase("Total", font));
        totalLabelCell.setColspan(2);
        totalLabelCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        totalLabelCell.setPadding(6);
        table.addCell(totalLabelCell);

        PdfPCell totalAmountCell = new PdfPCell(new Phrase(String.valueOf(totalAmount) + "€", font));
        totalAmountCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        totalAmountCell.setPadding(6);
        table.addCell(totalAmountCell);

        PdfPCell emptyCell = new PdfPCell(new Phrase("", font));
        emptyCell.setPadding(6);
        table.addCell(emptyCell);
    }

    private FranchiseeEntity fetchFranchisee(Long franchiseeId) {
        return franchiseeRepository.findByIdOptional(franchiseeId)
                .orElseThrow(() -> new NotFoundException("Franchisé introuvable"));
    }
}