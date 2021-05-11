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
            int index = originalName.lastIndexOf('/');
            String curPath = storageService.getPath(originalName).substring(0, index + 1) + "edit" + storageService.getPath(originalName).substring(index);
            Path path = Path.of(curPath);
            byte[] file = Files.readAllBytes(path);
            final ByteArrayResource byteArrayResource = new ByteArrayResource(file);
            storageService.deleteFile(originalName);
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
                                              @RequestParam(required = false, value = "style") MultipartFile style,
                                              @RequestParam(required = false) String style_id) {
        try {
            storageService.uploadImage(file);
            if (style != null) {
                storageService.uploadImage(style);
                storageService.editImageFile(file.getOriginalFilename(), style.getOriginalFilename());
                storageService.deleteFile(style.getOriginalFilename());
            } else if (style_id != null) {
                // TODO
            } else {
                return ResponseEntity.notFound().header("error", "No style or style_id").build();
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().header("error", "Error on save").build();
        }
        return ResponseEntity.ok("upload");
    }

}
