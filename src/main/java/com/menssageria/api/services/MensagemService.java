package com.menssageria.api.services;

import com.menssageria.api.domain.model.MensagemClinica;
import com.menssageria.api.domain.repository.MensagemClinicaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MensagemService {

    private final MensagemClinicaRepository repository;
    private final ZApiService zapiService;

    @Transactional
    public MensagemClinica enviarMensagem(MensagemClinica mensagem) {
        // Gera o Hash se não existir
        if (mensagem.getHash() == null) {
            mensagem.setHash(UUID.randomUUID().toString());
        }
        
        mensagem.setConfirmado(false);
        mensagem.setDataEnvio(LocalDateTime.now());
        
        // 1. Envia para o WhatsApp e CAPTURA o ID que a Z-API gera
        String messageId = zapiService.enviarComBotao(mensagem.getTelefone(), mensagem.getMensagem(), mensagem.getHash());
        
        // 2. Vincula o ID da Z-API ao nosso registro antes de salvar
        mensagem.setZapiMessageId(messageId);
        
        log.info("💾 Salvando mensagem no banco. ID Z-API: {}", messageId);
        return repository.save(mensagem);
    }

    // 🎯 ESTE É O ÚNICO MÉTODO DE CONFIRMAÇÃO POR HASH QUE DEVE EXISTIR
    @Transactional
    public void confirmarRecebimentoPorHash(String hash) {
        String hashLimpo = hash.trim();
        log.info("🎯 Clique no botão detectado! Processando Hash: [{}]", hashLimpo);
        
        // Tenta atualizar usando o LIKE para evitar erros de espaços
        int linhas = repository.confirmarPorHash(hashLimpo);
        
        if (linhas > 0) {
            log.info("✅ PERSISTÊNCIA OK! Coluna 'confirmado' agora é 1 no MySQL.");
        } else {
            log.warn("⚠️ FALHA: Hash [{}] não encontrado. Verifique se o registro existe no banco.", hashLimpo);
        }
    }

    // Métodos para Dashboard e Relatórios
    public List<MensagemClinica> listarPorConfirmacao(Boolean status) { 
        return repository.findByConfirmado(status); 
    }

    public List<MensagemClinica> listarRelatorioCompleto() { 
        return repository.findAllByOrderByDataEnvioDesc(); 
    }

    // Webhooks de Status (Apenas atualizam a data de leitura)
    @Transactional
    public void marcarComoLida(String zapiId) { 
        log.info("👀 Webhook: Lida {}", zapiId);
        repository.marcarComoConfirmada(zapiId); 
    }

    @Transactional
    public void confirmarRecebimento(String zapiId) { 
        log.info("🚚 Webhook: Entregue {}", zapiId);
        repository.marcarComoConfirmada(zapiId); 
    }
}