package com.challengers.photocheck.repository;

import com.challengers.photocheck.domain.PhotoCheckStatus;

import java.util.List;

public interface PhotoCheckRepositoryCustom {
    void updateStatusByIds(List<Long>photoCheckIds, PhotoCheckStatus photoCheckStatus);
}
