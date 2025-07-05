package com.tarea4.tarea4.repositories;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.tarea4.tarea4.models.ActividadTema;

@Repository
public class ActividadTemaRepository {

    public Optional<ActividadTema> findFirstByActividadId(int actividadId) {
        throw new UnsupportedOperationException("Unimplemented method 'findFirstByActividadId'");
    }

    public void save(ActividadTema actividadTema) {
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

}
