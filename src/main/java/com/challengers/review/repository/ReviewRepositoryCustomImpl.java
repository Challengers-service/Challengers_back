package com.challengers.review.repository;

import com.challengers.review.domain.QReview;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

import java.util.Objects;

import static com.challengers.review.domain.QReview.*;

@Repository
public class ReviewRepositoryCustomImpl implements ReviewRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    public ReviewRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public float getStarRatingAverageByChallengeId(Long challengeId) {
        Double starRatingAvg = queryFactory
                .select(review.starRating.avg())
                .from(review)
                .where(review.challenge.id.eq(challengeId))
                .fetchOne();

        return starRatingAvg == null ? 0.0f : starRatingAvg.floatValue();
    }
}
