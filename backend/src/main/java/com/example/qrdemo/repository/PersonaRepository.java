package com.example.qrdemo.repository;

import com.example.qrdemo.model.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// Repositorio para la entidad Persona, con operaciones CRUD
public interface PersonaRepository extends JpaRepository<Persona, Long> {

    // Busca una persona por su cédula de identidad
    Optional<Persona> findByCi(String ci);
}
