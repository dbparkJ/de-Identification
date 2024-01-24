package com.example.deidentification.headerController.fragmentController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DocsPageController {
    @GetMapping("/docsPage")
    public String docsPage() {
        return "pages/docsPage";
    }
}
