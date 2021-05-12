package by.bsu.neuralgallery.userfile.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@EnableAsync
@Service
public class StorageService {

    @Value("${filePath}")
    private String path;

    @Value("${neuralPath}")
    private String neuralPath;

    public String uploadImage(MultipartFile imageFile) {
        try {
            byte[] bytes = imageFile.getBytes();
            Path filePath = Path.of(path + imageFile.getOriginalFilename());
            Files.write(filePath, bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageFile.getOriginalFilename();

    }

    public String getPath() {
        return path;
    }

    @Async
    public void deleteFile(String path) {
        Path globalPath = Path.of(path);
        try {
            Files.delete(globalPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Async
    public void editImageFile(String filePathOrig, String filePathStyle, boolean deleteStyle) {
        ArrayList<String> commands = new ArrayList<>();
        commands.add("python3");
        commands.add(neuralPath);
        commands.add("-file_orig=" + path + filePathOrig);
        commands.add("-file_style=" + path + filePathStyle);

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
        deleteFile(path + filePathOrig);
        if (deleteStyle) {
            deleteFile(path + filePathStyle);
        }
    }

    public boolean isFileExist(String originalName) {
        File tmpFile = new File(originalName);
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
