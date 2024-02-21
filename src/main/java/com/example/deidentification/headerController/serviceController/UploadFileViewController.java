package com.example.deidentification.headerController.serviceController;

import com.example.deidentification.service.FileService;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import io.tus.java.client.ProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UploadFileViewController {

    private final FileService fileService;

    @Autowired
    public UploadFileViewController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/fileuploadPage")
    public String fileuploadPage(){
        return "/pages/deIdentification_fileupload";
    }
    @PostMapping("/uploadFile")
    public ResponseEntity<Map<String, Object>> uploadFile(@RequestParam("file") MultipartFile multipartFile,
                                                          RedirectAttributes redirectAttributes) throws ProtocolException, IOException {

        Map<String, Object> response = new HashMap<>();

        if (multipartFile.isEmpty()) {
            response.put("success", false);
            response.put("message", "파일을 선택해주세요.");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            fileService.tusFileUpload(multipartFile, redirectAttributes);
            // DB 로직
            fileService.saveDb(multipartFile);
            response.put("success", true);
            response.put("message", "파일 업로드에 성공했습니다.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "파일 업로드 중 오류가 발생했습니다.");
            return ResponseEntity.badRequest().body(response);
        }
    }

}

