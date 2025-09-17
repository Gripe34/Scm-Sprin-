package com.miproyecto.demo.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        String redirectURL = request.getContextPath();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority auth : authorities) {
            String role = auth.getAuthority();

            if (role.equals("cliente")) {
                redirectURL += "/cliente/index";
                break;
            } else if (role.equals("veterinario")) {
                redirectURL += "/veterinarios/index";
                break;
            } else if (role.equals("administrador")) {
                redirectURL += "/admin/index";
                break;
            }
        }

        response.sendRedirect(redirectURL);
    }
}

