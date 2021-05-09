package com.molistry.userfile.web;

import com.molistry.userfile.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/api/userfiles")
@RequiredArgsConstructor
public class UserFileController {

    private final StorageService storageService;

    @GetMapping("/file")
    public ResponseEntity<ByteArrayResource> getFile(@RequestParam String originalName) {
        try {

            Path path = Path.of(storageService.getPath(originalName));
            byte[] file = Files.readAllBytes(path);
            final ByteArrayResource byteArrayResource = new ByteArrayResource(file);
            storageService.deleteFile(path);
            return ResponseEntity.ok()
                    .contentLength(file.length)
                    .contentType(MediaType.valueOf(Files.probeContentType(path)))
                    .body(byteArrayResource);
        } catch (IOException e) {
            return ResponseEntity.notFound().header("error", "File not found").build();
        }

    }

    @GetMapping
    public ResponseEntity<Boolean> isFileExist(@RequestParam String originalName) {
        return ResponseEntity.ok(storageService.isFileExist(originalName));
    }


    @PostMapping
    public ResponseEntity<String> uploadImage(@RequestPart(value = "imageFile") MultipartFile file,
                                              @RequestPart String originalName) {

        return ResponseEntity.ok(storageService.uploadImage(file, originalName));
    }

}
