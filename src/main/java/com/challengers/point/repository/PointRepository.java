package com.challengers.point.repository;

import com.challengers.point.domain.Point;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PointRepository extends JpaRepository<Point,Long>, PointRepositoryCustom {
    Optional<Point> findByUserId(Long userId);
}
