package com.challengers.examplephoto.repository;

import com.challengers.examplephoto.domain.ExamplePhoto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamplePhotoRepository extends JpaRepository<ExamplePhoto,Long> {
}
