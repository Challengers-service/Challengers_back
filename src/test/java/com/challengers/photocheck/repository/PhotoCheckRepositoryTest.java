package com.challengers.photocheck.repository;

import com.challengers.photocheck.domain.PhotoCheck;
import com.challengers.photocheck.domain.PhotoCheckStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.Arrays;


@DataJpaTest
public class PhotoCheckRepositoryTest {
    @Autowired PhotoCheckRepository photoCheckRepository;

    @BeforeEach
    void setUp() {
        PhotoCheck photoCheck1 = PhotoCheck.builder()
                .id(1L)
                .status(PhotoCheckStatus.WAITING)
                .build();
        PhotoCheck photoCheck2 = PhotoCheck.builder()
                .id(2L)
                .status(PhotoCheckStatus.WAITING)
                .build();

        photoCheckRepository.save(photoCheck1);
        photoCheckRepository.save(photoCheck2);
    }

    @Test
    @DisplayName("인증 사진 상태 변경")
    void updateStatusByIds() {
        ArrayList<Long> photoCheckIds = new ArrayList<>(Arrays.asList(1L,2L));
        photoCheckRepository.updateStatusByIds(photoCheckIds, PhotoCheckStatus.PASS);

        photoCheckIds
                .forEach(id -> Assertions.assertThat(photoCheckRepository.findById(id).get().getStatus())
                        .isEqualTo(PhotoCheckStatus.PASS));
    }
}
