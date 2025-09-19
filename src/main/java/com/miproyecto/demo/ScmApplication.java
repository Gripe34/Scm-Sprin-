package com.miproyecto.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class ScmApplication implements CommandLineRunner {
    @Autowired
    private PasswordEncoder passwordEncoder;
    public static void main(String[] args) {
        SpringApplication.run(ScmApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        String passwordPlano = "1234";
        String passwordHasheado = passwordEncoder.encode(passwordPlano);

        System.out.println("=============================================================");
        System.out.println("               GENERADOR DE CONTRASEÑAS HASHEDAS             ");
        System.out.println("-------------------------------------------------------------");
        System.out.println("Contraseña en texto plano: " + passwordPlano);
        System.out.println("Contraseña Hasheada (BCrypt): " + passwordHasheado);
        System.out.println("=============================================================");

    }
}
