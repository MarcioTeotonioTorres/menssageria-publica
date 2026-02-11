package com.menssageria.api.controller;

import com.menssageria.api.services.MensagemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/publico")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MensagemPublicaController {

    private final MensagemService service;

    @GetMapping("/confirmar/{hash}")
    public ResponseEntity<String> confirmarRecebimento(@PathVariable String hash) {
        service.confirmarRecebimentoPorHash(hash);
 
        return ResponseEntity.ok("""
            <!DOCTYPE html>
            <html lang="pt-br">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Confirmação de Agendamento</title>
                <style>
                    body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f4f7f6; display: flex; justify-content: center; align-items: center; height: 100vh; margin: 0; }
                    .card { background: white; padding: 40px; border-radius: 20px; box-shadow: 0 10px 25px rgba(0,0,0,0.1); text-align: center; max-width: 400px; width: 90%; }
                    .icon { font-size: 60px; color: #28a745; margin-bottom: 20px; }
                    h1 { color: #333; font-size: 24px; margin-bottom: 10px; }
                    p { color: #666; line-height: 1.6; }
                    .footer { margin-top: 30px; font-size: 12px; color: #999; }
                </style>
            </head>
            <body>
                <div class="card">
                    <div class="icon">✔</div>
                    <h1>Presença Confirmada!</h1>
                    <p>Olá! Sua confirmação foi registrada com sucesso. <br> Agradecemos sua atenção e nos vemos em breve na clínica.</p>
                    <div class="footer">Sistema de Mensageria Clínica</div>
                </div>
            </body>
            </html>
            """);
    }
}