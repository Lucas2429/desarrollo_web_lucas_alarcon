package com.tarea4.tarea4.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tarea4.tarea4.models.User;
import com.tarea4.tarea4.repositories.UserRepository;

@Controller
@RequestMapping("/user")
@PreAuthorize("hasRole('USER')")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public String profile(Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new RuntimeException("No User found for username: " + username);
        }

        model.addAttribute("user", user);
        return "user";
    }

}
