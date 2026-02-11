package com.menssageria.api.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

@Service
public class WhatsAppService {

    @Value("${zapi.url}")
    private String urlSend;

    @Value("${zapi.client-token}")
    private String clientToken;

    // Use a sua URL atual do ngrok
    private final String NGROK_URL = "https://rubie-nitrifiable-knowledgably.ngrok-free.dev";

 // Mude de 'void' para 'String'
    public String enviarViaZApi(String telefonePaciente, String mensagem, String hash) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Client-Token", clientToken);

        String urlConfirmacao = NGROK_URL + "/v1/publico/confirmar/" + hash;
        String mensagemFinal = mensagem + "\n\nConfirme aqui: " + urlConfirmacao;

        Map<String, String> body = new HashMap<>();
        body.put("phone", "55" + telefonePaciente.replaceAll("\\D", ""));
        body.put("message", mensagemFinal);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);

        try {
            // 🚀 Usando 'exchange' com 'ParameterizedTypeReference' para evitar avisos de Type Safety
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                urlSend,
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<Map<String, Object>>() {}
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                // Como o Map está tipado, o compilador fica feliz
                String idGerado = (String) response.getBody().get("messageId");
                System.out.println("✅ Z-API gerou o ID: " + idGerado);
                return idGerado;
            }
        } catch (Exception e) {
            System.err.println("❌ Erro Z-API: " + e.getMessage());
        }
        return null; 
    }
}