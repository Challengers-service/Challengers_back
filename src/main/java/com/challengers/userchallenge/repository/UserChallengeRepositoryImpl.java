package com.challengers.userchallenge.repository;


import com.challengers.challenge.domain.ChallengeStatus;
import com.challengers.challenge.domain.CheckFrequencyType;
import com.challengers.photocheck.domain.PhotoCheckStatus;
import com.challengers.user.domain.QUser;
import com.challengers.userchallenge.domain.UserChallenge;
import com.challengers.userchallenge.domain.UserChallengeStatus;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

import java.util.List;

import static com.challengers.challenge.domain.QChallenge.*;
import static com.challengers.photocheck.domain.QPhotoCheck.*;
import static com.challengers.user.domain.QUser.*;
import static com.challengers.userchallenge.domain.QUserChallenge.*;

@Repository
public class UserChallengeRepositoryImpl implements UserChallengeRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    public UserChallengeRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
        this.em = em;
    }


    @Override
    public Long updateStatusToSuccess(Long challengeId) {
        long updatedUserCount = queryFactory
                .update(userChallenge)
                .set(userChallenge.status, UserChallengeStatus.SUCCESS)
                .where(userChallenge.challenge.id.eq(challengeId)
                        .and(userChallenge.status.eq(UserChallengeStatus.IN_PROGRESS)))
                .execute();

        em.flush();
        em.clear();

        return updatedUserCount;
    }

    @Override
    public long updateStatusToFail(List<Long> successIds) {
        long cnt = queryFactory
                .update(userChallenge)
                .set(userChallenge.status, UserChallengeStatus.FAIL)
                .where(userChallenge.status.eq(UserChallengeStatus.IN_PROGRESS).and(userChallenge.id.notIn(successIds)))
                .execute();

        em.flush();
        em.clear();

        return cnt;
    }

    @Override
    public UserChallenge getChallengeById(Long userChallengeId) {
        return queryFactory
                .selectFrom(userChallenge)
                .join(userChallenge.challenge, challenge).fetchJoin()
                .where(userChallenge.id.eq(userChallengeId))
                .fetchOne();
    }

    @Override
    public Long getSumSuccessProgress(Long challengeId) {
        return queryFactory
                .select(userChallenge.maxProgress.sum().longValue())
                .from(userChallenge)
                .where(userChallenge.challenge.id.eq(challengeId), userChallenge.status.eq(UserChallengeStatus.SUCCESS))
                .fetchOne();
    }

    @Override
    public List<String> getProfileImagesLimit2(Long challengeId) {
        return queryFactory
                .select(userChallenge.user.image)
                .from(userChallenge)
                .join(userChallenge.user, user)
                .limit(2L)
                .fetch();
    }

    /*
    select uc.user_challenge_id
    from challenge c join user_challenge uc on c.challenge_id = uc.challenge_id left join photo_check p on uc.user_challenge_id = p.user_challenge_id and c.round = p.round
    left join photo_check p2 on p.photo_check_id = p2.photo_check_id
    where c.status = "IN_PROGRESS" and (isNull(p.status) or p.status<>"FAIL")
    group by uc.user_challenge_id, c.check_times_per_round
    having count(p.photo_check_id) < c.check_times_per_round
     */
    @Override
    public List<Long> getSuccessIds(boolean isMonday) {
        return queryFactory
                .select(userChallenge.id)
                .from(userChallenge)
                .join(userChallenge.challenge,challenge)
                .leftJoin(photoCheck).on(photoCheck.round.eq(challenge.round).and(userChallenge.id.eq(photoCheck.userChallenge.id)))
                .where(userChallenge.status.eq(UserChallengeStatus.IN_PROGRESS),challenge.status.eq(ChallengeStatus.IN_PROGRESS),mondayConditionBuilder(isMonday),photoCheck.status.ne(PhotoCheckStatus.FAIL))
                .groupBy(userChallenge.id, challenge.checkTimesPerRound)
                .having(photoCheck.id.count().intValue().eq(challenge.checkTimesPerRound))
                .fetch();

    }



    private BooleanBuilder mondayConditionBuilder(boolean isMonday) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        return booleanBuilder
                .and(challengeCheckFrequencyCondition(isMonday));
    }

    private BooleanExpression challengeCheckFrequencyCondition(boolean isMonday) {
        return isMonday ? null : challenge.checkFrequencyType.eq(CheckFrequencyType.EVERY_DAY);
    }
}
