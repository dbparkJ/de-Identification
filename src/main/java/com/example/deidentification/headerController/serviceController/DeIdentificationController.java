package com.example.deidentification.headerController.serviceController;

import com.example.deidentification.service.FileService;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import io.tus.java.client.ProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
public class DeIdentificationController {

    private final FileService fileService;

    @Autowired
    public DeIdentificationController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/deIdentification")
    public String deIdentification(Model model) throws JSchException, SftpException {
        fileService.fileThumbnail(model);
        return "/pages/deIdentification";
    }
    @PostMapping("/uploadFile")
    public String uploadFile(@RequestParam("file") MultipartFile multipartFile,
                             RedirectAttributes redirectAttributes)
            throws ProtocolException, IOException {

        if (multipartFile.isEmpty()) {
            redirectAttributes.addFlashAttribute("upload_message", "파일을 선택해주세요.");
            return "redirect:deIdentification";
        }

        fileService.tusFileUpload(multipartFile, redirectAttributes);
        // DB 로직
        fileService.saveDb(multipartFile);

        return "/pages/deIdentification";
    }

}

