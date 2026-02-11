package com.menssageria.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class AuditConfig {

	@Bean
	AuditorAware<String> auditorProvider() {
		return () -> {
			// Acessa o contexto de segurança do Spring Security
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

			// Verifica se existe um usuário autenticado e se não é um usuário anônimo
			if (authentication == null || !authentication.isAuthenticated()
					|| authentication.getPrincipal().equals("anonymousUser")) {
				return Optional.of("SISTEMA"); // Valor padrão caso não haja login (ex: processos automáticos)
			}

			// Retorna o username extraído do JWT/Login
			return Optional.of(authentication.getName());
		};
	}
}