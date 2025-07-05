package com.tarea4.tarea4.services;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ApiService {

    public Map<String, Object> getActividadesPorDia() {
        List<String> dias = new ArrayList<>();
        List<Integer> cantidades = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            String dia = LocalDate.of(2025, 7, i).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            dias.add(dia);
            cantidades.add(new Random().nextInt(10) + 1); // de 1 a 10
        }

        Map<String, Object> result = new HashMap<>();
        result.put("dias", dias);
        result.put("cantidades", cantidades);
        return result;
    }

    public Map<String, Object> getActividadesPorTipo() {
        List<Map<String, Object>> tipos = new ArrayList<>();
        List<String> nombres = List.of("música", "deporte", "ciencias", "religión", "política",
                "tecnología", "juegos", "baile", "comida", "otro");

        for (String tipo : nombres) {
            Map<String, Object> dato = new HashMap<>();
            dato.put("name", tipo);
            dato.put("y", new Random().nextInt(15) + 5); // de 5 a 20
            tipos.add(dato);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("tipos", tipos);
        return result;
    }

    public Map<String, Object> getActividadesPorMesFranja() {
        List<String> meses = List.of("Enero", "Febrero", "Marzo", "Abril", "Mayo");
        List<Integer> manana = new ArrayList<>();
        List<Integer> mediodia = new ArrayList<>();
        List<Integer> tarde = new ArrayList<>();

        for (int i = 0; i < meses.size(); i++) {
            manana.add(new Random().nextInt(10) + 1);
            mediodia.add(new Random().nextInt(10) + 1);
            tarde.add(new Random().nextInt(10) + 1);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("meses", meses);
        result.put("manana", manana);
        result.put("mediodia", mediodia);
        result.put("tarde", tarde);
        return result;
    }
}

