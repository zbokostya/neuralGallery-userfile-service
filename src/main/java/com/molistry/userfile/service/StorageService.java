package com.molistry.userfile.service;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
public class StorageService {

    @Value("${filePath}")
    private String path;

    public String uploadImage(MultipartFile imageFile, String originalName) {
        try {
            byte[] bytes = imageFile.getBytes();
            Path filePath = Path.of(path + originalName);
            Files.write(filePath, bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return originalName;

    }

    public String getPath(String originalName) {
        return path + originalName;
    }

    public void deleteFile(Path path) {
        try {
            Files.delete(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // TODO
    public void editImage(String filePath) {
        ArrayList<String> commands = new ArrayList<>();
        commands.add("python3");
        commands.add("/Users/kostyaz/dev/curs3sem/molistry-userfile-service/1.py");
        commands.add("img/" + filePath);
//        commands.add("1.py -img " + path + filePath);

        ProcessBuilder pb = new ProcessBuilder(commands).inheritIO();
        try {
            Process p = pb.start();
            BufferedReader bfr = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = "";
            System.out.println("Running Python starts: " + line);
            int exitCode = p.waitFor();
            System.out.println("Exit Code : " + exitCode);
            line = bfr.readLine();
            System.out.println("First Line: " + line);
            while ((line = bfr.readLine()) != null) {
                System.out.println("Python Output: " + line);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean isFileExist(String originalName) {
        File tmpFile = new File(path + originalName);
        return tmpFile.isFile();
    }


    private List<MediaType> getSupportedMediaTypes() {
        List<MediaType> list = new ArrayList<MediaType>();
        list.add(MediaType.IMAGE_JPEG);
        list.add(MediaType.IMAGE_PNG);
        list.add(MediaType.APPLICATION_OCTET_STREAM);
        return list;
    }

}
