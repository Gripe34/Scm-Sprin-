package com.miproyecto.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cliente")
public class ClienteController {

    @GetMapping("/index")
    public String mostrarClienteDashboard() {
        return "cliente/index"; // esto carga templates/cliente/index.html
    }
}
