package com.challengers.challengephoto.repository;

import com.challengers.challengephoto.domain.ChallengePhoto;
import com.challengers.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengePhotoRepository extends JpaRepository<ChallengePhoto,Long> {
    Page<ChallengePhoto> findAllByUser(Pageable pageable, User user);
}
