package com.miproyecto.demo.service;

import java.util.Map;

public interface PdfGenerationService {

    /**
     * Contrato que define un método para generar un PDF a partir de una plantilla HTML.
     */
    byte[] generarPdfDesdeHtml(String templateNombre, Map<String, Object> datos);

}