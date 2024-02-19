package com.example.deidentification.headerController.serviceController;

import com.example.deidentification.service.FileService;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

//@Controller
//public class DeIdentificationViewController {
//    private FileService fileService;
//
//
//    public DeIdentificationViewController(FileService fileService) {
//        this.fileService = fileService;
//    }
//
//    @GetMapping("/deIdentification")
//    public String deidentificationViewer(Model model) throws JSchException, SftpException {
//        fileService.fileThumbnail(model);
//        return "/pages/deIdentification";
//    }
//}
