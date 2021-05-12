package by.bsu.neuralgallery.userfile.web;

import by.bsu.neuralgallery.userfile.service.StorageService;
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
            Path path = Path.of(storageService.getPath() + "edit_" + originalName);
            byte[] file = Files.readAllBytes(path);
            final ByteArrayResource byteArrayResource = new ByteArrayResource(file);
            storageService.deleteFile(storageService.getPath() + "edit_" + originalName);
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
        return ResponseEntity.ok(storageService.isFileExist(storageService.getPath() + "edit_" + originalName));
    }

    @PostMapping
    public ResponseEntity<String> uploadImage(@RequestPart(value = "imageFile") MultipartFile file,
                                              @RequestParam(required = false, value = "style") MultipartFile style,
                                              @RequestParam(required = false) String styleId) {
        try {
            storageService.uploadImage(file);
            if (style != null) {
                storageService.uploadImage(style);
                storageService.editImageFile(file.getOriginalFilename(), style.getOriginalFilename(), true);
            } else if (styleId != null) {
                storageService.editImageFile(file.getOriginalFilename(), "prepared_style_" + styleId + ".jpg", false);
            } else {
                return ResponseEntity.notFound().header("error", "No style or style id").build();
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().header("error", "Error on save").build();
        }
        return ResponseEntity.ok("upload");
    }

}
