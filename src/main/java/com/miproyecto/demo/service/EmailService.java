package com.miproyecto.demo.service;

import org.springframework.web.multipart.MultipartFile;

public interface EmailService {
    void enviarCorreoMasivo(String[] destinatarios, String asunto, String mensaje, MultipartFile adjunto);
}
