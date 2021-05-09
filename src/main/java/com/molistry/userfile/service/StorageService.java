package com.molistry.userfile.service;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
public class StorageService {


    private final String path = "/Users/kostyaz/dev/curs3sem/molistry-userfile-service/img/";

    public String uploadImage(MultipartFile imageFile, String name) {
        try {
            byte[] bytes = imageFile.getBytes();
            Path filePath = Path.of(path + name);
            Files.write(filePath, bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return name;

    }

    public void deleteFile(String path) {
        try {
            Path filePath = Path.of(path);
            Files.delete(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private List<MediaType> getSupportedMediaTypes() {
        List<MediaType> list = new ArrayList<MediaType>();
        list.add(MediaType.IMAGE_JPEG);
        list.add(MediaType.IMAGE_PNG);
        list.add(MediaType.APPLICATION_OCTET_STREAM);
        return list;
    }

}
