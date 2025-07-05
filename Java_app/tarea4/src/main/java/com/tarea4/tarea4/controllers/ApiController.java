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
    
    // @PostMapping("/api/evaluar/{actividadId}")
    // public ResponseEntity<Map<String, Object>> evaluarActividad(
    //        @PathVariable int actividadId,
    //        @RequestBody Map<String, Integer> body) {
        
    //    Integer nota = body.get("nota");
    //    if (nota == null || nota < 1 || nota > 7) {
    //        return ResponseEntity.badRequest().build();
    //    }

    //    Nota nuevaNota = new Nota();
    //    nuevaNota.setNota(nota);
    //    nuevaNota.setActividadId(actividadId);
    //    notaRepository.save(nuevaNota);

    //    double promedio = notaRepository.findByActividadId(actividadId)
    //        .stream()
    //        .mapToInt(Nota::getNota)
    //        .average()
    //        .orElse(0.0);

    //    Map<String, Object> response = new HashMap<>();
    //    response.put("promedio", Math.round(promedio * 10.0) / 10.0); // redondear 1 decimal

    //    return ResponseEntity.ok(response);
    //}


}


