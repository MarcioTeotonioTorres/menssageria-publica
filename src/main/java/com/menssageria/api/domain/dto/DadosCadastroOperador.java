package com.menssageria.api.domain.dto;


import com.menssageria.api.domain.model.UserRole;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DadosCadastroOperador(
    @NotBlank String nomeCompleto,
    @NotBlank String login,
    @NotBlank String senha,
    @NotNull UserRole role
) {
}