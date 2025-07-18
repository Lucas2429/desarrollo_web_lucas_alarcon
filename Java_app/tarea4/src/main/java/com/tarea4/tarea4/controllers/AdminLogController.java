package com.tarea4.tarea4.controllers;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tarea4.tarea4.models.Log;
import com.tarea4.tarea4.repositories.LogRepository;

@Controller
@RequestMapping("/log")
@PreAuthorize("hasRole('ADMINFOTO')")
public class AdminLogController {

    private final LogRepository logRepository;

    public AdminLogController(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @GetMapping("")
    public String listLogs(Model model){
        List<Log> logs = logRepository.findAll().reversed();
        model.addAttribute("logs", logs);
        return "log";
    }
}
