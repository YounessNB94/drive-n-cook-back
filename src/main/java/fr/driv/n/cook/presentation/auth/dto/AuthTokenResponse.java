package fr.driv.n.cook.presentation.auth.dto;

import java.time.Instant;

public record AuthTokenResponse(String accessToken, Instant expiresAt) {
}

