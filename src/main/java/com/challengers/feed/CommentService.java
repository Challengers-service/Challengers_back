package com.challengers.feed;

import com.challengers.common.exception.UserException;
import com.challengers.feed.dto.ChallengePhotoUserDto;
import com.challengers.feed.dto.CommentDto;
import com.challengers.feed.dto.CommentRequest;
import com.challengers.feed.dto.CommentResponse;
import com.challengers.user.domain.User;
import com.challengers.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public CommentResponse getComment(Long challengePhotoId){
        List<Comment> commentList = commentRepository.findByChallengePhotoIdOrderByIdDesc(challengePhotoId);
        List<CommentDto> comments = new ArrayList<>();

        for(Comment comment : commentList){
            User user = userRepository.findById(comment.getUserId()).orElseThrow(UserException::new);

            ChallengePhotoUserDto challengePhotoUserDto = ChallengePhotoUserDto.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .image(user.getImage())
                    .build();

            CommentDto commentDto = CommentDto.builder()
                    .id(comment.getId())
                    .auth(challengePhotoUserDto)
                    .content(comment.getContent())
                    .build();
            comments.add(commentDto);
        }

        Long commentCnt = commentRepository.countByChallengePhotoId(challengePhotoId);

        CommentResponse commentResponse = CommentResponse.builder()
                .comments(comments)
                .commentCnt(commentCnt)
                .build();

        return commentResponse;
    }

    @Transactional
    public void createComment(Long userId, Long challengePhotoId, CommentRequest commentRequest){
        Comment comment = Comment.builder()
                .userId(userId)
                .challengePhotoId(challengePhotoId)
                .content(commentRequest.getContent())
                .build();
        commentRepository.save(comment);
    }

    @Transactional
    public void updateComment(Long commentId, CommentRequest commentRequest){
        Comment comment = commentRepository.findById(commentId).get();
        comment.update(commentRequest.getContent());
    }

    @Transactional
    public void deleteComment(Long commentId){
        commentRepository.deleteById(commentId);
    }

}
