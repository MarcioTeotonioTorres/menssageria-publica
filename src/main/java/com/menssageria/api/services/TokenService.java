package com.menssageria.api.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.menssageria.api.domain.model.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.Instant; // Importe o Instant
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {
    
    @Value("${api.security.token.secret}")
    private String secret;

    public String gerarToken(Usuario usuario) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("clinica-whatsapp-api")
                    .withSubject(usuario.getLogin())
                    .withClaim("role", usuario.getRole().name()) 
                    // Alterado de 8 horas para 60 minutos
                    .withExpiresAt(dataExpiracao())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar token", exception);
        }
    }

    public String validarToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("clinica-whatsapp-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            return ""; 
        }
    }

    // Criamos um método privado para organizar o cálculo do tempo
    private Instant dataExpiracao() {
        return LocalDateTime.now().plusMinutes(60).toInstant(ZoneOffset.of("-03:00"));
    }
}