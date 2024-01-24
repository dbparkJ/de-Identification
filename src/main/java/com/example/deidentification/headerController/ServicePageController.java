package com.example.deidentification.headerController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ServicePageController {
    @GetMapping("/servicePage")
    public String servicePage() {
        return "pages/servicePage";
    }
}

