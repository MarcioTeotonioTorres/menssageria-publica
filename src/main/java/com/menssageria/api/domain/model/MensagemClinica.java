package com.menssageria.api.domain.model;

import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class MensagemClinica {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String paciente;
    private String telefone;
    private String mensagem;
    
    // Para o Webhook (Z-API)
    private String zapiMessageId; 
    
    // Para o Link (Clique do Paciente)
    private String hash; 
    
    // Status unificado
    @Builder.Default
    private Boolean confirmado = false;
    
    @Builder.Default
    private LocalDateTime dataEnvio = LocalDateTime.now();
    private LocalDateTime dataLeitura;

}