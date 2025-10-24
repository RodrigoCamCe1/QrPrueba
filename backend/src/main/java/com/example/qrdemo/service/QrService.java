package com.example.qrdemo.service;

import com.example.qrdemo.model.Persona;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Map;
import java.util.Optional;

// Interfaz de servicios para QR y gestión de personas
public interface QrService {

    // Genera un QR y guarda los datos de la persona
    byte[] generateAndSave(Map<String, Object> data) throws Exception;

    // Lee un QR desde la imagen y devuelve la persona correspondiente
    Optional<Persona> readFromImage(MultipartFile file) throws Exception;

    // Lista todas las personas registradas
    List<Persona> listAll();

    // Busca una persona por su ID
    Optional<Persona> findById(Long id);

    // Devuelve los bytes de la imagen QR de una persona
    byte[] qrImageBytes(Persona p);
}
