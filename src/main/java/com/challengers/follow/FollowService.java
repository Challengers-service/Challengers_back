package com.challengers.follow;


import com.challengers.common.exception.FollowException;
import com.challengers.common.exception.UserException;
import com.challengers.follow.domain.Follow;
import com.challengers.follow.dto.FollowResponse;
import com.challengers.user.domain.User;
import com.challengers.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class FollowService {

    private final UserRepository userRepository;

    private final FollowRepository followRepository;

    @Transactional(readOnly = true)
    public List<FollowResponse> findAllFollowers(Long userId){
        User user = userRepository.findById(userId).orElseThrow(UserException::new);

        List<FollowResponse> FollowList = followRepository.findAllByFromUser(user.getId());

        return FollowList;
    }

    @Transactional(readOnly = true)
    public List<FollowResponse> findAllFollowing(Long userId){
        User user = userRepository.findById(userId).orElseThrow(UserException::new);

        List<FollowResponse> FollowingList = followRepository.findAllByToUser(user.getId());

        return FollowingList;
    }

    @Transactional
    public void addFollow(Long toUserId, Long fromUserId){
        checkSameUser(toUserId, fromUserId);

        Optional<Follow> relation = getFollowRelation(toUserId, fromUserId);
        if(relation.isPresent())
            throw new FollowException("이미 follow 한 관계입니다.");

        followRepository.save(new Follow(toUserId, fromUserId));
    }

    @Transactional
    public void unFollow(Long toUserId, Long fromUserId){
        checkSameUser(toUserId, fromUserId);

        Optional<Follow> relation = getFollowRelation(toUserId, fromUserId);
        if(relation.isEmpty())
            throw new FollowException("follow 관계가 아닙니다.");

        followRepository.delete(relation.get());
    }

    private void checkSameUser(Long toUserId, Long fromUserId) {
        if(toUserId.equals(fromUserId)) throw new FollowException("follower 와 following 의 대상이 동일합니다.");
    }

    private Optional<Follow> getFollowRelation(Long toUserId, Long fromUserId) {
        return followRepository.findByToUserAndFromUser(toUserId, fromUserId);
    }
}
