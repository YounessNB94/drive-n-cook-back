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
import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("/reports")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class ReportResource {

    @Inject
    ReportService reportService;

    @Inject
    JsonWebToken jsonWebToken;

    @POST
    @Path("/me")
    @Produces("application/pdf")
    public Response requestMyReport(@Valid ReportRequest request) {
        ReportFile file = reportService.generateReport(currentFranchiseeId(), request);
        return buildPdfResponse(file);
    }

    @POST
    @Produces("application/pdf")
    public Response requestReportForAdmin(@Valid ReportRequest request) {
        if (request.franchiseeId() == null) {
            throw new BadRequestException("franchiseeId requis pour un rapport admin");
        }
        ReportFile file = reportService.generateReport(request.franchiseeId(), request);
        return buildPdfResponse(file);
    }

    private Response buildPdfResponse(ReportFile file) {
        return Response.ok(file.content())
                .type(file.contentType())
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.fileName() + "\"")
                .build();
    }

    private Long currentFranchiseeId() {
        String subject = jsonWebToken != null ? jsonWebToken.getSubject() : null;
        if (subject == null) {
            throw new NotAuthorizedException("Utilisateur non authentifié");
        }
        try {
            return Long.valueOf(subject);
        } catch (NumberFormatException ex) {
            throw new NotAuthorizedException("Identifiant utilisateur invalide", ex);
        }
    }
}
