package com.challengers.feed.service;

import com.challengers.challengephoto.domain.ChallengePhoto;
import com.challengers.challengephoto.repository.ChallengePhotoRepository;
import com.challengers.common.exception.UserException;
import com.challengers.feed.dto.ChallengePhotoUserDto;
import com.challengers.feed.dto.PostResponse;
import com.challengers.feed.repository.CommentRepository;
import com.challengers.feed.repository.LikeRepository;
import com.challengers.follow.FollowRepository;
import com.challengers.follow.dto.FollowResponse;
import com.challengers.user.domain.User;
import com.challengers.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PostService {
    private final UserRepository userRepository;
    private final ChallengePhotoRepository challengePhotoRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final FollowRepository followRepository;

    @Transactional
    public List<PostResponse> getAllPosts(Pageable pageable) {
        Page<ChallengePhoto> list = challengePhotoRepository.findAll(pageable);
        return fetchPosts(list);
    }

    @Transactional
    public PostResponse getOnePost(Long ChallengePhotoId) {
        ChallengePhoto challengePhoto = challengePhotoRepository.findById(ChallengePhotoId).orElseThrow(NoSuchElementException::new);
        return fetchPost(challengePhoto);
    }

    @Transactional
    public List<PostResponse> getUserPosts(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId).orElseThrow(UserException::new);
        Page<ChallengePhoto> list = challengePhotoRepository.findAllByUser(pageable, user);
        return fetchPosts(list);
    }

    @Transactional
    public List<PostResponse> getFollowingPosts(Long userId, Pageable pageable) {
        List<PostResponse> posts = new ArrayList<>();
        List<FollowResponse> FollowingList = followRepository.findAllByToUser(userId);

        for(FollowResponse followResponse : FollowingList){
            User user = userRepository.findById(followResponse.getId()).orElseThrow(UserException::new);
            Page<ChallengePhoto> list = challengePhotoRepository.findAllByUser(pageable, user);
            for (ChallengePhoto challengePhoto : list.getContent()){
                posts.add(fetchPost(challengePhoto));
            }
        }

        return posts;
    }

    public List<PostResponse> fetchPosts(Page<ChallengePhoto> list){
        List<PostResponse> posts = new ArrayList<>();

        for (ChallengePhoto challengePhoto : list.getContent()){
            posts.add(fetchPost(challengePhoto));
        }

        return posts;
    }

    public PostResponse fetchPost(ChallengePhoto challengePhoto){
        Long userId = challengePhoto.getUser().getId();
        String userName = challengePhoto.getUser().getName();
        String userImageUrl = challengePhoto.getUser().getImage();

        Long challengePhotoId = challengePhoto.getId();
        String image = challengePhoto.getPhotoUrl();
        String title = challengePhoto.getChallenge().getName();

        ChallengePhotoUserDto challengePhotoUserDto = ChallengePhotoUserDto.builder()
                .id(userId)
                .name(userName)
                .image(userImageUrl)
                .build();

        Long commentCnt = commentRepository.countByChallengePhotoId(challengePhotoId);
        Long likeCnt = likeRepository.countByChallengePhotoId(challengePhotoId);

        PostResponse postResponse = PostResponse.builder()
                .id(challengePhotoId)
                .ChallengePhotoUserDto(challengePhotoUserDto)
                .title(title)
                .image(image)
                .commentCnt(commentCnt)
                .likeCnt(likeCnt)
                .build();

        return postResponse;
    }
}
