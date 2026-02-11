package com.menssageria.api.security;

import com.menssageria.api.domain.repository.UsuarioRepository;
import com.menssageria.api.services.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String uri = request.getRequestURI();

        // 🚀 PASSO 1: IGNORAR VALIDAÇÃO PARA ROTAS PÚBLICAS
        // Isso mata o erro 403 no ngrok para a Z-API
        if (uri.startsWith("/api/webhooks") || uri.startsWith("/v1/publico")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 🔐 PASSO 2: VALIDAÇÃO DO TOKEN JWT
        var token = recuperarToken(request);

        if (token != null && !token.isEmpty()) {
            // Usando o método que você postou: validarToken(token)
            var login = tokenService.validarToken(token); 
            
            if (!login.isEmpty()) {
                // Buscando o usuário pelo login (assumindo que seu repository tem findByLogin)
                UserDetails usuario = usuarioRepository.findByLogin(login);

                if (usuario != null) {
                    var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null) {
            return authorizationHeader.replace("Bearer ", "");
        }
        return null;
    }
}