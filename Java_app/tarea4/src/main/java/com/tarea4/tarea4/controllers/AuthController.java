package com.tarea4.tarea4.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tarea4.tarea4.models.Role;
import com.tarea4.tarea4.models.User;
import com.tarea4.tarea4.repositories.UserRepository;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam("username") String username,
                            @RequestParam("password") String password) {
        if (userRepository.findByUsername(username) != null) {
            return "redirect:/register?error"; // Handle error feedback
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(Role.ROLE_USER);

        userRepository.save(user);
        return "redirect:/login";
    }

}
