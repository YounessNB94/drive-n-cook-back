package fr.driv.n.cook.presentation.appointment.dto;

import fr.driv.n.cook.presentation.shared.dto.AppointmentStatus;

import java.time.LocalDateTime;

public record AppointmentPatch(
        LocalDateTime datetime,
        AppointmentStatus status
) {
}
