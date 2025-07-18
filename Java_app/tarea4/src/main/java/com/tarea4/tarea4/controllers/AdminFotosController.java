package com.tarea4.tarea4.controllers;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tarea4.tarea4.models.Foto;
import com.tarea4.tarea4.models.Log;
import com.tarea4.tarea4.repositories.FotoRepository;
import com.tarea4.tarea4.repositories.LogRepository;

@Controller
@RequestMapping("/admin-fotos")
@PreAuthorize("hasRole('ADMINFOTO')")
public class AdminFotosController {

    private final LogRepository logRepository;

    private final FotoRepository fotoRepository;

    public AdminFotosController(LogRepository logRepository, FotoRepository fotoRepository) {
        this.logRepository = logRepository;
        this.fotoRepository = fotoRepository;
    }

    @GetMapping("/")
    public String listFotos(Model model){
        List<Foto> fotos = fotoRepository.findAll().reversed();
        model.addAttribute("fotos", fotos);
        return "admin-fotos";
    }
    
    @PostMapping("/eliminar-foto")
    public String eliminarFoto(@RequestParam("id") Integer id, @RequestParam("motivo") String motivo) {

        fotoRepository.deleteById(id);
        
        Log log = new Log();
        String mensaje = "Eliminado foto " + id + " por usuario admin, motivo: " + motivo;
        log.setMensaje(mensaje);
                                
        logRepository.save(log);
        return "redirect:/";
    }
}
