package com.menssageria.api.domain.repository;

import com.menssageria.api.domain.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    // Este método é crucial para o Spring Security encontrar o operador pelo login
    UserDetails findByLogin(String login);
}