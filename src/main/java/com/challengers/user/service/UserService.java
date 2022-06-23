package com.challengers.user.service;

import com.challengers.common.AwsS3Uploader;
import com.challengers.common.exception.ResourceNotFoundException;
import com.challengers.user.domain.User;
import com.challengers.user.dto.UserUpdateDto;
import com.challengers.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AwsS3Uploader awsS3Uploader;

    @Transactional
    public User getCurrentUser(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
    }

    @Transactional
    public void updateUser(Long userId, UserUpdateDto userUpdateDto){
        User user = userRepository.findById(userId).orElseThrow(NoSuchElementException::new);

        String changeName = userUpdateDto.getName();
        String changeBio = userUpdateDto.getBio();
        MultipartFile image = userUpdateDto.getImage();

        if(image == null){
            if(userUpdateDto.getIsImageChanged()){
                awsS3Uploader.deleteImage(user.getImage());
                user.update(changeName, changeBio, User.DEFAULT_IMAGE_URL);
            }else{
                user.update(changeName, changeBio, user.getImage());
            }
        }else{
            String imageUrl = awsS3Uploader.uploadImage(image);
            user.update(changeName, changeBio, imageUrl);
        }
    }
}
