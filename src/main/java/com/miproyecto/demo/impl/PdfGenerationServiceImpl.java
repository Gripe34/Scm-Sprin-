package com.miproyecto.demo.impl;

import com.lowagie.text.pdf.BaseFont;
import com.miproyecto.demo.service.PdfGenerationService; // Importa la interfaz
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.util.Map;

@Service
public class PdfGenerationServiceImpl implements PdfGenerationService { // <-- Implementa la interfaz

    @Autowired
    private TemplateEngine templateEngine;

    @Override // <-- Indica que estás implementando el método del "contrato"
    public byte[] generarPdfDesdeHtml(String templateNombre, Map<String, Object> datos) {
        Context context = new Context();
        context.setVariables(datos);

        String htmlProcesado = templateEngine.process(templateNombre, context);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlProcesado);
            renderer.layout();
            renderer.createPDF(outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error al generar el PDF", e);
        }
    }
}
