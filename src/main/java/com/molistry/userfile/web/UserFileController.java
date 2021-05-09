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
    private final String path = "/Users/kostyaz/dev/curs3sem/molistry-userfile-service/img/";

    @GetMapping
    public ResponseEntity<ByteArrayResource> getFile(@RequestParam String originalName) {
        try {

            byte[] file = Files.readAllBytes(Path.of(path + originalName));
            final ByteArrayResource byteArrayResource = new ByteArrayResource(file);
            storageService.deleteFile(path + originalName);
            return ResponseEntity.ok()
                    .contentLength(file.length)
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(byteArrayResource);
        } catch (IOException e) {
            return ResponseEntity.notFound().header("error", "File not found").build();
        }

    }

    @PostMapping
    public ResponseEntity<String> uploadImage(@RequestPart(value = "imageFile") MultipartFile file,
                                              @RequestPart String originalName) {

        return ResponseEntity.ok(storageService.uploadImage(file, originalName));
    }

}
