package com.challengers.user.service;

import com.challengers.common.AwsS3Uploader;
import com.challengers.common.exception.ResourceNotFoundException;
import com.challengers.common.exception.UserException;
import com.challengers.follow.FollowRepository;
import com.challengers.user.domain.Award;
import com.challengers.user.domain.User;
import com.challengers.user.dto.UserMeResponse;
import com.challengers.user.dto.UserUpdateRequest;
import com.challengers.user.repository.AchievementRepository;
import com.challengers.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final AchievementRepository achievementRepository;
    private final AwsS3Uploader awsS3Uploader;

    @Transactional
    public UserMeResponse getCurrentUser(Long userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new UserException());
        Long followerCount = followRepository.countByFromUser(userId);
        Long followingCount = followRepository.countByToUser(userId);
        List<Award> awardList = achievementRepository.findAllByUser(user);
        return UserMeResponse.builder()
                .user(user)
                .followerCount(followerCount)
                .followingCount(followingCount)
                .awardList(awardList)
                .build();
    }

    @Transactional
    public void updateUser(Long userId, UserUpdateRequest userUpdateRequest){
        User user = userRepository.findById(userId).orElseThrow(NoSuchElementException::new);

        String changeName = userUpdateRequest.getName();
        String changeBio = userUpdateRequest.getBio();
        MultipartFile image = userUpdateRequest.getImage();

        if(image == null){
            if(userUpdateRequest.getIsImageChanged()){
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
