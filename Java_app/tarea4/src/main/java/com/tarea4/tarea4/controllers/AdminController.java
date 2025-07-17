package com.tarea4.tarea4.controllers;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.tarea4.tarea4.models.Foto;
import com.tarea4.tarea4.models.User;
import com.tarea4.tarea4.repositories.FotoRepository;
import com.tarea4.tarea4.repositories.UserRepository;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
// @PreAuthorize("hasAuthority('ROLE_ADMIN')") --> Es lo mismo!
public class AdminController {

    private final UserRepository userRepository;

    private final FotoRepository fotoRepository;

    public AdminController(UserRepository userRepository, FotoRepository fotoRepository) {
        this.userRepository = userRepository;
        this.fotoRepository = fotoRepository;
    }

    @GetMapping("/")
    public String listUsers(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "admin";
    }

    @GetMapping("/admin-fotos")
    public String listFotos(Model model){
        List<Foto> fotos = fotoRepository.findAll();
        model.addAttribute("fotos", fotos);
        return "admin-fotos";
    }
}
