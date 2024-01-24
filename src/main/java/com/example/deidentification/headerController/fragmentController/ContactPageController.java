package com.example.deidentification.headerController.fragmentController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ContactPageController {
    @GetMapping("/contactPage")
    public String contactPage() {
        return "pages/contactPage";
    }
}
