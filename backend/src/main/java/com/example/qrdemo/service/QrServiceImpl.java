package com.example.qrdemo.service;

import com.example.qrdemo.model.Persona;
import com.example.qrdemo.repository.PersonaRepository;
import com.example.qrdemo.util.QrUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

// Implementación de QrService, no entendí muy bien por qué era necesaria esta Impl honestamente,
// Sé que tiene que ver con la inyección de dependencias y la gestión de la DB pero no lo comprendo del todo 
@Service
public class QrServiceImpl implements QrService {

    private final PersonaRepository personaRepository;
    private final ObjectMapper objectMapper;

    public QrServiceImpl(PersonaRepository personaRepository, ObjectMapper objectMapper) {
        this.personaRepository = personaRepository;
        this.objectMapper = objectMapper;
    }

    // Genera QR, crea persona y guarda en DB
    @Override
    public byte[] generateAndSave(Map<String, Object> datos) throws Exception {
        String ci = (String) datos.get("ci");
        if (ci == null || ci.isBlank()) throw new IllegalArgumentException("ci requerido");

        Persona p = new Persona();
        p.setNombre((String) datos.get("nombre"));
        p.setApellido((String) datos.get("apellido"));
        p.setCi(ci);
        p.setSexo((String) datos.get("sexo"));
        p.setDireccion((String) datos.get("direccion"));
        p.setCelular((String) datos.get("celular"));
        p.setFechaRegistro(LocalDateTime.now());
        String json = objectMapper.writeValueAsString(datos);
        p.setQrContenido(json);

        personaRepository.save(p);

        return QrUtils.generateQRCodeImage(json, 300, 300);
    }

    // Lee QR desde imagen y obtiene persona por ci
    @Override
    public Optional<Persona> readFromImage(MultipartFile file) throws Exception {
        String decoded = QrUtils.decodeQRCode(file);
        if (decoded == null) return Optional.empty();
        Map<String,Object> map = objectMapper.readValue(decoded, Map.class);
        String ci = (String) map.get("ci");
        if (ci == null) return Optional.empty();
        return personaRepository.findByCi(ci);
    }

    // Lista todas las personas
    @Override
    @Transactional(readOnly = true)
    public List<Persona> listAll() {
        return personaRepository.findAll();
    }

    // Busca persona por ID
    @Override
    @Transactional(readOnly = true)
    public Optional<Persona> findById(Long id) {
        return personaRepository.findById(id);
    }

    // Genera bytes de QR desde un objeto Persona
    @Override
    public byte[] qrImageBytes(Persona p) {
        if (p == null || p.getQrContenido() == null) return null;
        try {
            return QrUtils.generateQRCodeImage(p.getQrContenido(), 300, 300);
        } catch (Exception ex) {
            return null;
        }
    }
}
