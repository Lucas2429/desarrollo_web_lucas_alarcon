package com.tarea4.tarea4.models;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "log")
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private LocalDateTime fecha;

    @Column(nullable = false, length = 300)
    private String mensaje;

    @PrePersist
    protected void onCreate() {
        this.fecha = LocalDateTime.now();
    }

    public Log(){
    }

    public Log(LocalDateTime fecha, String mensaje){
        this.fecha = fecha;
        this.mensaje = mensaje;
    }

    // getters y setters
    public Integer getId() { return id; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
}


