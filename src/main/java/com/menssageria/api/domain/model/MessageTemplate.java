package com.menssageria.api.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "templates")
@Getter @Setter 
@NoArgsConstructor @AllArgsConstructor
public class MessageTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name; // Ex: confirmacao_consulta

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content; // Texto com os {{1}}, {{2}}

    @Enumerated(EnumType.STRING)
    private TemplateStatus status = TemplateStatus.PENDING;

    public enum TemplateStatus {
        PENDING, APPROVED, REJECTED
    }
}