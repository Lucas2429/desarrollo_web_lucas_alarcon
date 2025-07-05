package com.tarea4.tarea4.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tarea4.tarea4.models.Comuna;

import java.util.Optional;

public interface ComunaRepository extends JpaRepository<Comuna, Integer> {
    Optional<Comuna> findByNombre(String nombre);
}

