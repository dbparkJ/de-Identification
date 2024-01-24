package com.example.deidentification.headerController.fragmentController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IntroPageController {
    @GetMapping("/intro")
    public String intro() {
        return "pages/intro";
    }
}
