package com.example.deidentification.headerController.fragmentController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ServicePageController {
    @GetMapping("/servicePage")
    public String servicePage() {
        return "pages/serviceMainPage";
    }
}

