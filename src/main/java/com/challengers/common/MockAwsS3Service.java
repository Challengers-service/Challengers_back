package com.challengers.common;


import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Profile({"test","local"})
@Component
@AllArgsConstructor
public class MockAwsS3Service implements AwsS3Service {

    private final static String imageUrl = "https://challengers-bucket.s3.ap-northeast-2.amazonaws.com/mockImageUrl.PNG";
    @Override
    public String uploadImage(MultipartFile multipartFile) {
        return imageUrl;
    }

    @Override
    public List<String> uploadImages(List<MultipartFile> multipartFile) {
        return new ArrayList<>(Arrays.asList(imageUrl,imageUrl,imageUrl));
    }

    @Override
    public void deleteImage(String fileUrl) {

    }

    @Override
    public void deleteImages(List<String> fileUrls) {

    }
}
