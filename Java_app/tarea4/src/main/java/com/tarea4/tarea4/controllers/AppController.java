package com.tarea4.tarea4.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.tarea4.tarea4.models.Actividad;
import com.tarea4.tarea4.services.AppService;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.*;

@Controller
public class AppController {

    private final AppService appService;
    public AppController(AppService appService) {
        this.appService = appService;
    }

    @GetMapping({"/", "/home"})
    public String index(Model model) {
        List<Actividad> actividades = appService.getUltimasActividades(5);
        model.addAttribute("actividades", actividades);
        return "home";
    }

    @GetMapping("/actividades")
    public String actividades(Model model, @RequestParam(defaultValue = "1") int page) {
        Map<String, Object> datos = appService.getActividadesPaginadas(page);
        model.addAttribute("actividades_on_page", datos.get("actividades_on_page"));
        model.addAttribute("page", page);
        model.addAttribute("total_pages", datos.get("total_pages"));
        model.addAttribute("total_actividades", datos.get("total_actividades"));
        return "actividades";
    }

    @GetMapping("/estadisticas")
    public String estadisticas() {
        return "estadisticas";
    }

    @GetMapping("/agregar_actividad")
    public String agregarActividad() {
        return "agregar_actividad";
    }

    @PostMapping("/post_actividad")
    public String postActividad(
            @RequestParam String comuna,
            @RequestParam String sector,
            @RequestParam String nombre,
            @RequestParam String email,
            @RequestParam(required = false) String telefono,
            @RequestParam String contacto,
            @RequestParam String idContacto,
            @RequestParam LocalDateTime inicio,
            @RequestParam LocalDateTime termino,
            @RequestParam String descripcion,
            @RequestParam String tema,
            @RequestParam(required = false) String otroTema,
            @RequestParam("fotos") MultipartFile[] fotos

    ) throws NoSuchAlgorithmException, IOException {
        appService.crearActividad(comuna, sector, nombre, email, telefono,
                contacto, idContacto, inicio, termino, descripcion,
                tema, otroTema, fotos);

        return "redirect:/actividades";
    }
}
