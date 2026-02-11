package com.menssageria.api.controller;

import com.menssageria.api.domain.model.MensagemClinica;
import com.menssageria.api.services.MensagemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mensagens")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class MensagemController {

    private final MensagemService service;

    @PostMapping("/enviar")
    public ResponseEntity<MensagemClinica> enviar(@RequestBody MensagemClinica mensagem) {
        MensagemClinica salva = service.enviarMensagem(mensagem);
        return ResponseEntity.status(HttpStatus.CREATED).body(salva);
    }

    @GetMapping("/relatorio")
    public ResponseEntity<List<MensagemClinica>> relatorio() {
        return ResponseEntity.ok(service.listarRelatorioCompleto());
    }
    
    // MANTENHA APENAS ESTE MÉTODO PARA FILTRAR (Remova o outro chamado 'filtrar')
    @GetMapping("/confirmacao/{status}")
    public ResponseEntity<List<MensagemClinica>> listarPorConfirmacao(@PathVariable Boolean status) {
        return ResponseEntity.ok(service.listarPorConfirmacao(status));
    }
}