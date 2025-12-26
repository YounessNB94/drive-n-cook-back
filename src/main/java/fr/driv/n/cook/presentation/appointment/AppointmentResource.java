package fr.driv.n.cook.presentation.appointment;

import fr.driv.n.cook.presentation.appointment.dto.Appointment;
import fr.driv.n.cook.presentation.appointment.dto.AppointmentPatch;
import fr.driv.n.cook.shared.AppointmentStatus;
import fr.driv.n.cook.shared.AppointmentType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.time.LocalDateTime;

@Path("/appointments")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class AppointmentResource {

    @POST
    public Appointment createAppointment(@Valid Appointment appointment) {
        return new Appointment(
                1L,
                appointment.type(),
                appointment.warehouseId(),
                appointment.supplyOrderId(),
                appointment.truckId(),
                appointment.datetime(),
                AppointmentStatus.SCHEDULED,
                LocalDateTime.now()
        );
    }

    @PATCH
    @Path("/{appointmentId}")
    public Appointment updateAppointment(
            @PathParam("appointmentId") Long appointmentId,
            @Valid AppointmentPatch appointmentPatch
    ) {
        Appointment appointment = stubAppointment(appointmentId, AppointmentType.SUPPLY_PICKUP);
        LocalDateTime datetime = appointmentPatch.datetime() != null ? appointmentPatch.datetime() : appointment.datetime();
        AppointmentStatus status = appointmentPatch.status() != null ? appointmentPatch.status() : appointment.status();
        return new Appointment(
                appointment.id(),
                appointment.type(),
                appointment.warehouseId(),
                appointment.supplyOrderId(),
                appointment.truckId(),
                datetime,
                status,
                appointment.createdAt()
        );
    }

    private Appointment stubAppointment(Long id, AppointmentType type) {
        return new Appointment(
                id,
                type,
                5L,
                type == AppointmentType.SUPPLY_PICKUP ? 10L : null,
                type == AppointmentType.TRUCK_PICKUP ? 7L : null,
                LocalDateTime.now().plusDays(1),
                AppointmentStatus.SCHEDULED,
                LocalDateTime.now()
        );
    }
}
