package com.menssageria.api.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.menssageria.api.domain.model.MensagemClinica;
import com.menssageria.api.domain.model.MessageTemplate;
import com.menssageria.api.domain.repository.TemplateRepository;
import com.menssageria.api.services.MensagemService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/templates")
@CrossOrigin(origins = "http://localhost:4200") // Libera o seu Angular
@RequiredArgsConstructor
public class TemplateController {


	@Autowired
	private MensagemService mensagemService;
	
	@PostMapping("/enviar")
    public ResponseEntity<Map<String, String>> enviar(@RequestBody MensagemClinica mensagem) {
        // Chama o serviço para disparar no WhatsGW
       mensagemService.enviarMensagem(mensagem);

        Map<String, String> response = new HashMap<>();
        response.put("status", "Mensagem enviada para processamento");
        return ResponseEntity.ok(response);
    }
	
	private final TemplateRepository repository;


	@GetMapping
	public List<MessageTemplate> listar() {
		return repository.findAll();
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public MessageTemplate adicionar(@RequestBody MessageTemplate template) {
		return repository.save(template);
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		repository.deleteById(id);
	}
	
	@PutMapping("/{id}")
	public MessageTemplate atualizar(@PathVariable Long id, @RequestBody MessageTemplate templateAtualizado) {
	    // Buscamos o registro existente para garantir que ele existe
	    return repository.findById(id).map(template -> {
	        template.setName(templateAtualizado.getName());
	        template.setContent(templateAtualizado.getContent());
	        return repository.save(template);
	    }).orElseThrow(() -> new RuntimeException("Template não encontrado"));
	}
}