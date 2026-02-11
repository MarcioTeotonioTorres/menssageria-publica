package com.menssageria.api.controller;

import com.menssageria.api.domain.repository.MensagemClinicaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.transaction.Transactional;
import java.util.Map;

@RestController
@RequestMapping("/api/webhooks")
public class WebhookController {

    @Autowired
    private MensagemClinicaRepository repository;

    @PostMapping("/zapi")
    @Transactional
    public ResponseEntity<Void> receberStatusZapi(
            @RequestHeader(value = "client-token", required = false) String token,
            @RequestBody Map<String, Object> payload) {

        // Log para monitorar no console do IntelliJ/Eclipse
        System.out.println("🔔 Webhook da Z-API recebido!");
        
        String messageId = (String) payload.get("messageId");
        String status = (String) payload.get("status");

        // Se a mensagem foi lida ou entregue, atualizamos o banco
        if ("READ".equals(status) || "DELIVERED".equals(status)) {
            repository.marcarComoConfirmada(messageId);
            System.out.println("✅ Mensagem " + messageId + " atualizada para CONFIRMADA.");
        }

        return ResponseEntity.ok().build();
    }
}