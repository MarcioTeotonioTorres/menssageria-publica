package com.menssageria.api.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import lombok.extern.slf4j.Slf4j;
import java.util.*;

@Service
@Slf4j
public class ZApiService {

    @Value("${zapi.instance}") private String instancia;
    @Value("${zapi.token}") private String token;
    @Value("${zapi.client-token}") private String clientToken;
    @Value("${app.url.base}") private String urlBaseNgrok;

    private final RestClient restClient = RestClient.create();

    public String enviarComBotao(String telefone, String texto, String hash) {
        try {
            String telLimpo = telefone.replaceAll("\\D", "");
            if (!telLimpo.startsWith("55")) telLimpo = "55" + telLimpo;

            String urlZapi = String.format("https://api.z-api.io/instances/%s/token/%s/send-button-actions", instancia, token);
            String link = urlBaseNgrok + "/v1/publico/confirmar/" + hash;

            Map<String, Object> body = new HashMap<>();
            body.put("phone", telLimpo);
            body.put("message", texto);
            
            Map<String, Object> button = Map.of("type", "URL", "label", "Confirmar Presença ✅", "url", link);
            body.put("buttonActions", List.of(button));

            // 🎯 CAPTURANDO A RESPOSTA PARA PEGAR O ID
            var response = restClient.post().uri(urlZapi)
                .header("Client-Token", clientToken)
                .body(body)
                .retrieve()
                .body(Map.class);

            if (response != null && response.containsKey("messageId")) {
                return response.get("messageId").toString(); // Devolve o ID da Z-API
            }
            return null;
        } catch (Exception e) {
            log.error("❌ Erro Z-API: {}", e.getMessage());
            return null;
        }
    }
}