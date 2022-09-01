package com.challengers.common;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface AwsS3Service {
    String uploadImage(MultipartFile multipartFile);

    List<String> uploadImages(List<MultipartFile> multipartFile);

    void deleteImage(String fileUrl);

    void deleteImages(List<String> fileUrls);
}
