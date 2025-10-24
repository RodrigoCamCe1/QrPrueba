package com.example.qrdemo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity                      
@Table(name = "persona")     
public class Persona {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID autoincremental
    private Long id;

    // Campos de información personal
    private String nombre;
    private String apellido;
    
    @Column(unique = true)   // El campo CI debe ser único en la base de datos
    private String ci;
    private String sexo;
    private String direccion;
    private String celular;
    private LocalDateTime fechaRegistro;

    @Column(columnDefinition = "text") // Contenido del QR guardado como texto largo (JSON)
    private String qrContenido;
    
    // Métodos getter y setter 
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getCi() { return ci; }
    public void setCi(String ci) { this.ci = ci; }
    public String getSexo() { return sexo; }
    public void setSexo(String sexo) { this.sexo = sexo; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getCelular() { return celular; }
    public void setCelular(String celular) { this.celular = celular; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }
    public String getQrContenido() { return qrContenido; }
    public void setQrContenido(String qrContenido) { this.qrContenido = qrContenido; }
}
