package com.miproyecto.demo.impl;


import com.miproyecto.demo.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void enviarCorreoMasivo(String[] destinatarios, String asunto, String mensaje, MultipartFile adjunto) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();

            // Usamos MimeMessageHelper para soportar multipartes (texto y adjuntos)
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom("tu-correo@gmail.com"); // El remitente
            helper.setTo(destinatarios);
            helper.setSubject(asunto);
            helper.setText(mensaje, true); // true indica que el mensaje puede ser HTML

            // Si hay un archivo adjunto, lo añadimos
            if (adjunto != null && !adjunto.isEmpty()) {
                helper.addAttachment(adjunto.getOriginalFilename(), adjunto);
            }

            mailSender.send(mimeMessage);

        } catch (Exception e) {
            throw new RuntimeException("Error al enviar el correo masivo: " + e.getMessage(), e);
        }
    }
}