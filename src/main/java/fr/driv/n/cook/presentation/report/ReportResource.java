package fr.driv.n.cook.presentation.report;

import fr.driv.n.cook.presentation.report.dto.Report;
import fr.driv.n.cook.presentation.report.dto.ReportFile;
import fr.driv.n.cook.presentation.report.dto.ReportRequest;
import fr.driv.n.cook.service.report.ReportService;
import jakarta.annotation.security.RolesAllowed;
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
    @Path("/me")
    @Produces("application/pdf")
    public Response requestMyReport(@Valid ReportRequest request) {
        ReportFile file = reportService.generateReport(currentFranchiseeId(), request);
        return buildPdfResponse(file);
    }

    @POST
    @RolesAllowed("ADMIN")
    @Produces("application/pdf")
    public Response requestReportForAdmin(@Valid ReportRequest request) {
        if (request.franchiseeId() == null) {
            throw new BadRequestException("franchiseeId requis pour un rapport admin");
        }
        ReportFile file = reportService.generateReport(request.franchiseeId(), request);
        return buildPdfResponse(file);
    }

    @GET
    @Path("/{reportId}")
    public Report getReport(@PathParam("reportId") Long reportId) {
        return reportService.getReport(reportId);
    }


    private Response buildPdfResponse(ReportFile file) {
        return Response.ok(file.content())
                .type(file.contentType())
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.fileName() + "\"")
                .build();
    }

    private Long currentFranchiseeId() {
        return 1L;
    }
}
