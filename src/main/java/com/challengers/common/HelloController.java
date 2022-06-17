package com.challengers.common;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class HelloController {
    private final AwsS3Uploader awsS3Uploader;

    @PostMapping("/images")
    public ResponseEntity<List<String>> upload(@RequestParam("images") List<MultipartFile> multipartFileList) throws IOException {
        return ResponseEntity.ok(awsS3Uploader.uploadImages(multipartFileList));
    }

    @DeleteMapping("/image")
    public String deleteFile(@RequestParam String storedFileUrl) {
        awsS3Uploader.deleteImage(storedFileUrl);
        return "test";
    }
}
