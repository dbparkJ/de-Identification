package com.example.deidentification.utils;

import com.jcraft.jsch.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

// https://hitomis.tistory.com/141 패키지 제작해보장
@Configuration
public class SFTPUtil {
    private Session session = null;
    private Channel channel = null;
    private ChannelSftp channelSftp = null;

    @Value("${json.filePath}")
    private String jsonFilePath;

    public void init(String host, String username, String password, int port) throws JSchException {
        JSch jSch = new JSch();
        try {
            session = jSch.getSession(username, host, port);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword(password);
            session.connect();
            channel = session.openChannel("sftp");
            channel.connect();

            if (channel != null) {
                channelSftp = (ChannelSftp) channel;
            } else {
                System.out.println("channel is null");
            }
        } catch (JSchException e) {
            System.out.println("Error message : " + e);
        }


    }


    /**
     * sftp file list load
     * Currently using the specified folder
     * */
    public void fileList(Model model) throws SftpException {
        channelSftp.cd("/home/node/app/files");
        Vector<ChannelSftp.LsEntry> fileList = channelSftp.ls("*");
        List<String> fileNames = new ArrayList<>();

        for (ChannelSftp.LsEntry entry : fileList) {
            fileNames.add(entry.getFilename());
        }

        model.addAttribute("fileList", fileNames);
    }


    /**
     * 추후 제작 해야하는 로직 db 또는 세션을 기반으로 사용자 별 폴더를 만들 수 있게 해야함
     *
     * */
    public void mkdir(String nowDir, String mkDirName) {
        try {
            channelSftp.cd(nowDir);
            channelSftp.mkdir(mkDirName);
        } catch (SftpException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 추후 제작 해야하는 로직 db 또는 세션을 기반으로 사용자 별 폴더를 지울 수 있게 해야함
     *
     * */
    public void deleteFolder(String targetFolder) {
        try {
            channelSftp.cd(targetFolder);
            channelSftp.cd("..");
            channelSftp.rmdir(targetFolder);
        } catch (SftpException e) {
            throw new RuntimeException(e);
        }
    }

    public void changeFileName() {
        try {
            channelSftp.cd("/home/node/app/files");
            Vector<ChannelSftp.LsEntry> fileList = channelSftp.ls("*.json");
            if (fileList != null) {
                for (ChannelSftp.LsEntry entry : fileList) {
                    String jsonFileName = jsonFilePath + entry.getFilename();
                    JSONObject jsonObject = BasicUtils.fetchJsonFromUrl(jsonFileName);
                    String oldName = jsonObject.getString("id");
                    String newName = jsonObject.getJSONObject("metadata").getString("filename");
                    channelSftp.rename(oldName, newName);
                    channelSftp.rm(entry.getFilename());
                }
            }
        } catch (SftpException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * session disconnect
     * */
    public void disconnection() {
        channelSftp.exit();
        channelSftp.quit();
        session.disconnect();
    }




}
