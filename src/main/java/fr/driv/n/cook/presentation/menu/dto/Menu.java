package fr.driv.n.cook.presentation.menu.dto;

import fr.driv.n.cook.shared.MenuStatus;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record Menu(
        Long id,
        @NotNull MenuStatus status,
        LocalDateTime updatedAt
) {
}
