package com.tarea4.tarea4.repositories;

import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.tarea4.tarea4.models.ActividadTema;

@Repository
public interface ActividadTemaRepository extends JpaRepository<ActividadTema, Long> {
    Optional<ActividadTema> findFirstByActividadId(Integer actividadId);
}
