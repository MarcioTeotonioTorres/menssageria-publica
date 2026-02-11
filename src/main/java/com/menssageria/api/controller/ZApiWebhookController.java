package com.menssageria.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.menssageria.api.services.MensagemService;
import lombok.RequiredArgsConstructor;
import java.util.Map;

@RestController
@RequestMapping("/api/webhooks")
@RequiredArgsConstructor
public class ZApiWebhookController {

    private final MensagemService mensagemService;

    @PostMapping("/receber")
    public ResponseEntity<Void> receberNotificacao(@RequestBody Map<String, Object> payload) {
        System.out.println("🔔 Webhook recebido: " + payload);

        String status = (String) payload.get("status");
        String telefone = (String) payload.get("phone");

        // 1. TRATAMENTO DE STATUS (READ, RECEIVED, SENT)
        // A Z-API pode enviar o ID em 'messageId' ou em uma lista 'ids'
        if (status != null) {
            Object idsRaw = payload.get("ids");
            String messageId = (String) payload.get("messageId");

            if ("READ".equals(status)) {
                if (idsRaw instanceof java.util.List<?> list) {
                    for (Object id : list) {
                        mensagemService.marcarComoLida(id.toString());
                    }
                } else if (messageId != null) {
                    mensagemService.marcarComoLida(messageId);
                }
            }
        } 
        
        // 2. TRATAMENTO DE RESPOSTA DE TEXTO (O paciente escreveu algo)
        // Se o payload contém 'text', significa que o usuário interagiu.
        // Usamos o telefone para confirmar, caso o status READ tenha falhado.
        if (payload.containsKey("text") && telefone != null) {
            System.out.println("💬 Paciente respondeu texto. Confirmando via telefone: " + telefone);
            mensagemService.confirmarRecebimento(telefone);
        }

        return ResponseEntity.ok().build();
    }
}