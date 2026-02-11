package com.menssageria.api.controller;


import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.menssageria.api.domain.dto.DadosCadastroOperador;
import com.menssageria.api.domain.model.Usuario;
import com.menssageria.api.domain.repository.UsuarioRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/operadores")
public class OperadorController {

    @Autowired
    private UsuarioRepository repository;

    @GetMapping
    public ResponseEntity<List<Usuario>> listarTodos() {
        return ResponseEntity.ok(repository.findAll());
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Serializable> cadastrar(@RequestBody @Valid DadosCadastroOperador dados) {
        // 1. Verifica se o login já existe para não duplicar
        if(repository.findByLogin(dados.login()) != null) {
            return ResponseEntity.badRequest().body("Este login já está em uso.");
        }
        
        // 2. Criptografa a senha (Nunca salve senha em texto puro!)
        String senhaCriptografada = new BCryptPasswordEncoder().encode(dados.senha());
        
        // 3. Cria a entidade Usuario
        Usuario novoUsuario = new Usuario();
        novoUsuario.setNomeCompleto(dados.nomeCompleto());
        novoUsuario.setLogin(dados.login());
        novoUsuario.setSenha(senhaCriptografada);
        novoUsuario.setRole(dados.role());
        
        repository.save(novoUsuario);
        
        return ResponseEntity.ok(novoUsuario);
    }
    
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> excluir(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}