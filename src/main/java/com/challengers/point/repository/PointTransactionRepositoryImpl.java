package com.challengers.point.repository;

import com.challengers.common.Querydsl4RepositorySupport;
import com.challengers.point.dto.PointTransactionResponse;
import com.challengers.point.domain.PointTransaction;
import com.challengers.point.dto.QPointTransactionResponse;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;

import static com.challengers.point.domain.QPointTransaction.*;


public class PointTransactionRepositoryImpl extends Querydsl4RepositorySupport implements PointTransactionRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public PointTransactionRepositoryImpl(EntityManager em) {
        super(PointTransaction.class);
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<PointTransactionResponse> getPointHistory(Pageable pageable, Long pointId) {
        return applyPagination(pageable, contentQuery -> contentQuery
                        .select(new QPointTransactionResponse(
                                pointTransaction.amount,
                                pointTransaction.createdAt,
                                pointTransaction.type,
                                pointTransaction.result
                        ))
                        .from(pointTransaction)
                        .where(pointTransaction.point.id.eq(pointId))
                        .orderBy(pointTransaction.id.desc()),

                countQuery -> countQuery
                        .select(pointTransaction.id)
                        .from(pointTransaction)
                        .where(pointTransaction.point.id.eq(pointId))
        );
    }
}
