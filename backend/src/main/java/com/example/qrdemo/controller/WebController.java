package com.example.qrdemo.controller;

import com.example.qrdemo.model.Persona;
import com.example.qrdemo.service.QrService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;   

@Controller 
public class WebController {
    private final QrService qrService;

    // Inyección del servicio QR mediante constructor
    public WebController(QrService qrService) {
        this.qrService = qrService;
    }

    // Página principal del sistema
    @GetMapping({"/", "/index"})
    public String index() { return "index"; }

    // Muestra el formulario para generar un nuevo código QR
    @GetMapping("/generate")
    public String generateForm() { return "generate"; }

    // Muestra el formulario para leer un código QR
    @GetMapping("/read")
    public String readForm() { return "read"; }

    // Muestra la vista de inicio de sesión personalizada
    @GetMapping("/login")
    public String login(Model model,
                       @RequestParam(value = "error", required = false) String error,
                       @RequestParam(value = "logout", required = false) String logout) {

        if (error != null) {
            model.addAttribute("error", "Credenciales inválidas");  // mensaje de error
        }

        if (logout != null) {
            model.addAttribute("message", "Has cerrado sesión exitosamente"); // mensaje de logout
        }

        return "login";
    }

    // Muestra la lista de personas y sus códigos QR almacenados
    @GetMapping("/qrs")
    public String listQrs(Model model) {
        List<Persona> list = qrService.listAll();      // Obtiene todas las personas registradas
        model.addAttribute("qrs", list);               // Envía la lista a la vista "qrs.html"
        return "qrs";
    }

    // Devuelve la imagen QR asociada a una persona específica
    @GetMapping("/qrs/{id}/image")
    @Transactional(readOnly = true)
    public ResponseEntity<byte[]> getQrImage(@PathVariable Long id) {
        Optional<Persona> opt = qrService.findById(id); // Busca la persona por ID
        if (opt.isEmpty()) {
            return ResponseEntity.notFound().build();   // Si no existe, retorna 404
        }

        byte[] img = qrService.qrImageBytes(opt.get()); // Genera el QR desde el contenido almacenado
        if (img == null) {
            return ResponseEntity.notFound().build();   // Si no se puede generar, retorna 404
        }

        // Devuelve la imagen QR en formato PNG
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(img);
    }
}
