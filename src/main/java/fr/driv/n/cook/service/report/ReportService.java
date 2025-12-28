package fr.driv.n.cook.service.report;

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

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@ApplicationScoped
public class ReportService {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Inject
    ReportRepository reportRepository;

    @Inject
    FranchiseeRepository franchiseeRepository;

    @Inject
    ReportMapper mapper;

    @Transactional
    public Report requestReport(Long franchiseeId, ReportRequest request) {
        FranchiseeEntity franchisee = fetchFranchisee(franchiseeId);
        ReportEntity entity = mapper.toEntity(request);
        entity.setFranchisee(franchisee);
        reportRepository.persist(entity);
        scheduleGeneration(entity.getId());
        return mapper.toDto(entity);
    }

    private void scheduleGeneration(Long reportId) {
        CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(1000); // simulation courte
                markReportReady(reportId);
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            }
        }, executor);
    }

    public Report getReport(Long reportId) {
        return mapper.toDto(fetchReport(reportId));
    }

    public List<Report> listReports(Long franchiseeId) {
        return reportRepository.listByFranchisee(franchiseeId).stream()
                .map(mapper::toDto)
                .toList();
    }

    @Transactional
    public Report markReportReady(Long reportId) {
        ReportEntity entity = fetchReport(reportId);
        entity.setStatus(ReportStatus.READY);
        entity.setFilePath("/tmp/report-" + reportId + ".pdf");
        return mapper.toDto(entity);
    }

    public ReportFile downloadFile(Long reportId) {
        ReportEntity entity = fetchReport(reportId);
        if (entity.getStatus() != ReportStatus.READY) {
            throw new IllegalStateException("Rapport pas encore prêt");
        }
        byte[] stubContent = ("Report " + reportId).getBytes(StandardCharsets.UTF_8);
        return new ReportFile("report-" + reportId + ".pdf", "application/pdf", stubContent);
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
