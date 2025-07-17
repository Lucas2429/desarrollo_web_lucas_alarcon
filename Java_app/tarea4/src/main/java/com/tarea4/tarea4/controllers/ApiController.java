package com.tarea4.tarea4.controllers;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// import com.tarea4.tarea4.models.Nota;
// import com.tarea4.tarea4.repositories.NotaRepository;
import com.tarea4.tarea4.services.ApiService;

import java.util.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://127.0.0.1", allowCredentials = "true")
public class ApiController {

    @Autowired
    private ApiService apiService;

    // @Autowired
    // private NotaRepository notaRepository;

    @GetMapping("/actividades-por-dia")
    public Map<String, Object> actividadesPorDia() throws InterruptedException {
        return apiService.getActividadesPorDia();
    }

    @GetMapping("/actividades-por-tipo")
    public Map<String, Object> actividadesPorTipo() throws InterruptedException {
        return apiService.getActividadesPorTipo();
    }

    @GetMapping("/actividades-por-mes")
    public Map<String, Object> actividadesPorMesFranja() throws InterruptedException {
        return apiService.getActividadesPorMesFranja();
    }

}


