package com.challengers.examplephoto.repository;

import com.challengers.examplephoto.domain.ExamplePhoto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExamplePhotoRepository extends JpaRepository<ExamplePhoto,Long> {
    List<ExamplePhoto> findByChallengeId(Long challengeId);
}
