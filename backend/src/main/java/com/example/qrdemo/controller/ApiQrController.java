package com.example.qrdemo.controller;

import com.example.qrdemo.model.Persona;
import com.example.qrdemo.service.QrService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Optional;

@RestController                     
@RequestMapping("/api/v1/qr")      
public class ApiQrController {
    private final QrService qrService;

    // Inyección del servicio QR mediante constructor
    public ApiQrController(QrService qrService) {
        this.qrService = qrService;
    }

    // Genera un código QR a partir de los datos recibidos en formato JSON
    @PostMapping(value = "/generate", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<byte[]> generate(@RequestBody Map<String,Object> body) throws Exception {
        byte[] png = qrService.generateAndSave(body);       // Genera y guarda en la BD
        return ResponseEntity.ok()
            .contentType(MediaType.IMAGE_PNG)               // Devuelve el QR como imagen PNG
            .body(png);
    }

    // Lee un archivo de imagen y decodifica el contenido del código QR
    @PostMapping(value = "/read", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> read(@RequestPart("file") MultipartFile file) throws Exception {
        Optional<Persona> p = qrService.readFromImage(file); // Busca la persona por CI dentro del QR
        if (p.isPresent()) return ResponseEntity.ok(p.get()); // Devuelve la persona encontrada
        return ResponseEntity.status(404).body(Map.of("error","no encontrado")); // No encontrado
    }
}
