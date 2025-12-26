package fr.driv.n.cook.presentation.report;

import fr.driv.n.cook.presentation.report.dto.Report;
import fr.driv.n.cook.presentation.report.dto.ReportFile;
import fr.driv.n.cook.presentation.report.dto.ReportRequest;
import fr.driv.n.cook.presentation.shared.dto.ReportStatus;
import fr.driv.n.cook.presentation.shared.dto.ReportType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Path("/reports")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class ReportResource {

    @POST
    public Report requestReport(@Valid ReportRequest request) {
        return stubReport(1L, request.type(), request.from(), request.to(), ReportStatus.PENDING);
    }

    @GET
    @Path("/{reportId}")
    public Report getReport(@PathParam("reportId") Long reportId) {
        return stubReport(reportId, ReportType.SALES_STATS, LocalDate.now().minusDays(7), LocalDate.now(), ReportStatus.READY);
    }

    @GET
    @Path("/{reportId}/file")
    @Produces("application/pdf")
    public Response downloadReportFile(@PathParam("reportId") Long reportId) {
        ReportFile file = stubReportFile(reportId);
        return Response.ok(file.content())
                .type(file.contentType())
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.fileName() + "\"")
                .build();
    }

    private Report stubReport(Long id, ReportType type, LocalDate from, LocalDate to, ReportStatus status) {
        return new Report(
                id,
                type,
                from,
                to,
                status,
                LocalDateTime.now()
        );
    }

    private ReportFile stubReportFile(Long reportId) {
        byte[] fakeBytes = "PDF".getBytes(StandardCharsets.UTF_8);
        return new ReportFile("report-" + reportId + ".pdf", "application/pdf", fakeBytes);
    }
}
