package fr.driv.n.cook.presentation.report;

import fr.driv.n.cook.presentation.report.dto.Report;
import fr.driv.n.cook.presentation.report.dto.ReportFile;
import fr.driv.n.cook.presentation.report.dto.ReportRequest;
import fr.driv.n.cook.service.report.ReportService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/reports")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class ReportResource {

    @Inject
    ReportService reportService;

    @POST
    public Report requestReport(@Valid ReportRequest request) {
        return reportService.requestReport(currentFranchiseeId(), request);
    }

    @GET
    @Path("/{reportId}")
    public Report getReport(@PathParam("reportId") Long reportId) {
        return reportService.getReport(reportId);
    }

    @GET
    @Path("/{reportId}/file")
    @Produces("application/pdf")
    public Response downloadReportFile(@PathParam("reportId") Long reportId) {
        ReportFile file = reportService.downloadFile(reportId);
        return Response.ok(file.content())
                .type(file.contentType())
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.fileName() + "\"")
                .build();
    }

    private Long currentFranchiseeId() {
        return 1L;
    }
}
