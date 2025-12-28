package fr.driv.n.cook.presentation.appointment;

import fr.driv.n.cook.presentation.appointment.dto.Appointment;
import fr.driv.n.cook.presentation.appointment.dto.AppointmentPatch;
import fr.driv.n.cook.service.appointment.AppointmentService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/appointments")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class AppointmentResource {

    @Inject
    AppointmentService appointmentService;

    @POST
    public Appointment createAppointment(@Valid Appointment appointment) {
        return appointmentService.create(appointment);
    }

    @PATCH
    @Path("/{appointmentId}")
    public Appointment updateAppointment(
            @PathParam("appointmentId") Long appointmentId,
            @Valid AppointmentPatch appointmentPatch
    ) {
        return appointmentService.patch(appointmentId, appointmentPatch);
    }
}
