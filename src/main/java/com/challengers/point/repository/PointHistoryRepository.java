package com.challengers.point.repository;

import com.challengers.point.domain.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointHistoryRepository extends JpaRepository<PointHistory,Long>, PointHistoryRepositoryCustom {
}
