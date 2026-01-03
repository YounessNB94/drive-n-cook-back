package fr.driv.n.cook.presentation.appointment;

import fr.driv.n.cook.presentation.appointment.dto.Appointment;
import fr.driv.n.cook.presentation.appointment.dto.AppointmentPatch;
import fr.driv.n.cook.service.appointment.AppointmentService;
import fr.driv.n.cook.shared.AppointmentType;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.time.LocalDateTime;
import java.util.List;

@Path("/appointments")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class AppointmentResource {

    @Inject
    AppointmentService appointmentService;

    @Inject
    SecurityIdentity securityIdentity;

    @POST
    public Appointment createAppointment(@Valid Appointment appointment) {
        return appointmentService.create(appointment, currentFranchiseeId());
    }

    @PATCH
    @Path("/{appointmentId}")
    public Appointment updateAppointment(
            @PathParam("appointmentId") Long appointmentId,
            @Valid AppointmentPatch appointmentPatch
    ) {
        return appointmentService.patch(appointmentId, appointmentPatch);
    }

    @GET
    @Path("/me")
    public List<Appointment> listMyAppointments(
            @QueryParam("type") AppointmentType type,
            @QueryParam("warehouseId") Long warehouseId,
            @QueryParam("from") LocalDateTime from,
            @QueryParam("to") LocalDateTime to
    ) {
        return appointmentService.listForFranchisee(currentFranchiseeId(), type, warehouseId, from, to);
    }

    @GET
    @Path("/{appointmentId}")
    public Appointment getAppointment(@PathParam("appointmentId") Long appointmentId) {
        Appointment appointment = appointmentService.getById(appointmentId);
        if (!isAdmin()) {
            appointmentService.assertAppointmentBelongsToFranchisee(appointmentId, currentFranchiseeId());
        }
        return appointment;
    }

    @GET
    @RolesAllowed("ADMIN")
    public List<Appointment> listAppointments(
            @QueryParam("type") AppointmentType type,
            @QueryParam("warehouseId") Long warehouseId,
            @QueryParam("from") LocalDateTime from,
            @QueryParam("to") LocalDateTime to
    ) {
        return appointmentService.listForAdmin(type, warehouseId, from, to);
    }

    private boolean isAdmin() {
        return securityIdentity != null && securityIdentity.hasRole("ADMIN");
    }

    private Long currentFranchiseeId() {
        return 1L;
    }
}

