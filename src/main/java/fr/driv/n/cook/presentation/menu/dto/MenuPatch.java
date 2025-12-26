package fr.driv.n.cook.presentation.menu.dto;

import fr.driv.n.cook.presentation.shared.dto.MenuStatus;
import jakarta.validation.constraints.NotNull;

public record MenuPatch(
        @NotNull MenuStatus status
) {
}
