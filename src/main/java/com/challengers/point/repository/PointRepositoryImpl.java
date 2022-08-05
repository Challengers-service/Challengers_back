package com.challengers.point.repository;

import com.challengers.userchallenge.domain.UserChallengeStatus;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;

import static com.challengers.point.domain.QPoint.point1;
import static com.challengers.user.domain.QUser.user;
import static com.challengers.userchallenge.domain.QUserChallenge.userChallenge;

public class PointRepositoryImpl implements PointRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    public PointRepositoryImpl(EntityManager em) {
        queryFactory = new JPAQueryFactory(em);
        this.em = em;
    }

    @Override
    public void giveReward(Long challengeId, Long reward) {
        queryFactory
                .update(point1)
                .set(point1.point, point1.point.add(reward))
                .where(point1.userId.in(
                        JPAExpressions.select(user.id)
                                .from(userChallenge.user, user)
                                .where(userChallenge.status.eq(UserChallengeStatus.SUCCESS)
                                        .and(userChallenge.challenge.id.eq(challengeId)))
                ))
                .execute();

        em.flush();
        em.clear();
    }
}
