package com.tarea4.tarea4.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tarea4.tarea4.models.Comentario;

import java.util.List;

public interface ComentarioRepository extends JpaRepository<Comentario, Integer> {
    List<Comentario> findByActividadId(Integer actividadId);
}
