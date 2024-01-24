package com.example.deidentification.headerController.serviceController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DeIdentificationController {
    @GetMapping("/deIdentification")
    public String deIdentification() {
        return "/pages/deIdentification";
    }
}
