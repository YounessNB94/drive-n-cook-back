package fr.driv.n.cook.presentation.appointment.dto;

import fr.driv.n.cook.shared.AppointmentStatus;
import fr.driv.n.cook.shared.AppointmentType;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record Appointment(
        Long id,
        @NotNull AppointmentType type,
        @NotNull Long warehouseId,
        Long supplyOrderId,
        Long truckId,
        @NotNull LocalDateTime datetime,
        AppointmentStatus status,
        LocalDateTime createdAt
) {
}
