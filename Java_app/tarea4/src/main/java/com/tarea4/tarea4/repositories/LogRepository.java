package com.tarea4.tarea4.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.tarea4.tarea4.models.Log;

public interface LogRepository extends JpaRepository<Log, Long> {
    List<Log> findAllByOrderByFechaDesc();
}

