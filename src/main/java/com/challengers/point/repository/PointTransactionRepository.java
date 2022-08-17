package com.challengers.point.repository;

import com.challengers.point.domain.PointTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointTransactionRepository extends JpaRepository<PointTransaction,Long>, PointTransactionRepositoryCustom {
}
