package com.example.Proyecto_Final_Progra3.controller.freelance;

import com.example.Proyecto_Final_Progra3.service.freelance.FreelancerService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/freelancer")
public class FreelancerController {
    private final FreelancerService service;
    public FreelancerController(FreelancerService service) {
        this.service = service;
    }
}
