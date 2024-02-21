package com.example.deidentification.service;

import com.example.deidentification.entity.FileEntity;
import com.example.deidentification.repository.FileRepository;
import com.example.deidentification.utils.SFTPUtil;
import com.jcraft.jsch.*;
import io.tus.java.client.*;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.InputStream;
import java.net.ProtocolException;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Vector;


//fileService부분 대대적인 리팩토링 해야함
@Service
public class FileService {

    private final FileRepository fileRepository;

    private final SFTPUtil sftpUtil;

    @Value("${tus.filePath}")
    private URL tusURL;

    @Value("${nas.host}")
    private String host;
    @Value("${nas.port}")
    private int port;
    @Value("${nas.userId}")
    private String id;
    @Value("${nas.userPw}")
    private String pw;

    @Getter
    private String filetype;

    public String setFiletype(String filename) {
        String[] parts = filename.split("\\.");
        if (parts.length > 1) {
            this.filetype = parts[parts.length - 1];
        } else {
            this.filetype = "";
        }
        return filetype;
    }

    @Autowired
    public FileService(FileRepository fileRepository, SFTPUtil sftpUtil) {
        this.fileRepository = fileRepository;
        this.sftpUtil = sftpUtil;
    }


    public Map<String, String> FileMetaData(MultipartFile multipartFile) {

        String filename = multipartFile.getOriginalFilename();
        String filetype = setFiletype(Objects.requireNonNull(filename));
        Long filesize = multipartFile.getSize();

        Map<String, String> metaData = new HashMap<>();
        metaData.put("filename",filename);
        metaData.put("filetype",filetype);
        metaData.put("filesize",String.valueOf(filesize));

        return metaData;
    }
    public FileEntity saveDb(MultipartFile multipartFile) {
        FileEntity file = new FileEntity();
        file.setFileName(multipartFile.getOriginalFilename());
        file.setUploadTime(LocalDate.now());
        file.setFilePath(String.valueOf(tusURL));
        return fileRepository.save(file);
    }

    public void tusFileUpload(MultipartFile multipartFile, RedirectAttributes redirectAttributes)
            throws IOException, io.tus.java.client.ProtocolException {
        TusClient client = new TusClient();
        client.setUploadCreationURL(tusURL);
        client.enableResuming(new TusURLMemoryStore());

        InputStream inputStream = multipartFile.getInputStream();
        long totalBytes = multipartFile.getSize();

        final TusUpload upload = new TusUpload(); //멀티파트 파일 사용자로 부터 입력 받은거 넣어야함
        upload.setInputStream(inputStream);
        upload.setSize(totalBytes);
        upload.setFingerprint(multipartFile.getName());
        upload.setMetadata(FileMetaData(multipartFile));

        TusExecutor executor = new TusExecutor() {
            @Override
            protected void makeAttempt() throws ProtocolException, IOException, io.tus.java.client.ProtocolException {
                TusUploader uploader = client.resumeOrCreateUpload(upload);

                uploader.setChunkSize(1024 * 1024);
                while(uploader.uploadChunk() > -1){};
                // Allow the HTTP connection to be closed and cleaned up
                uploader.finish();
                redirectAttributes.addFlashAttribute("upload_message", "File uploaded successfully : " + multipartFile.getOriginalFilename());
            }
        };
        executor.makeAttempts();
    }

    /**
     * 서버에 있는 파일의 리스트와 썸네일을 가져오는 코드
     * */
    public void fileThumbnail(Model model) throws JSchException, SftpException {
        sftpUtil.init(host, id, pw, port);
        sftpUtil.changeFileName();
        sftpUtil.fileList(model);
        sftpUtil.disconnection();
    }
}
