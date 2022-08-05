package com.challengers.challenge.repository;

import com.challengers.challenge.domain.Category;
import com.challengers.challenge.domain.Challenge;
import com.challengers.challenge.domain.ChallengeStatus;
import com.challengers.challenge.domain.CheckFrequencyType;
import com.challengers.challenge.dto.ChallengeSearchCondition;
import com.challengers.common.Querydsl4RepositorySupport;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

import java.time.LocalDate;

import static com.challengers.challenge.domain.QChallenge.*;
import static com.challengers.challengetag.domain.QChallengeTag.challengeTag;
import static com.challengers.tag.domain.QTag.*;
import static org.springframework.util.StringUtils.hasText;

public class ChallengeRepositoryImpl extends Querydsl4RepositorySupport implements ChallengeRepositoryCustom {

    public ChallengeRepositoryImpl() {
        super(Challenge.class);
    }

    @Override
    public Page<Challenge> search(ChallengeSearchCondition condition, Pageable pageable) {
        return applyPagination(pageable, contentQuery -> contentQuery
                        .select(challenge).distinct()
                        .from(challenge)
                        .join(challenge.challengeTags.challengeTags, challengeTag)
                        .join(challengeTag.tag, tag)
                        .where(searchCond(condition),
                                challenge.status.in(ChallengeStatus.READY, ChallengeStatus.IN_PROGRESS)),

                countQuery -> countQuery
                        .select(challenge).distinct()
                        .from(challenge)
                        .join(challenge.challengeTags.challengeTags, challengeTag)
                        .join(challengeTag.tag, tag)
                        .where(searchCond(condition)
                                ,challenge.status.in(ChallengeStatus.READY, ChallengeStatus.IN_PROGRESS))
        );
    }

    @Override
    public long updateRound(boolean isMonday) {
        long updatedCount = update(challenge)
                .set(challenge.round, challenge.round.add(1))
                .where(challenge.status.eq(ChallengeStatus.IN_PROGRESS),
                        mondayConditionBuilder(isMonday))
                .execute();

        getEntityManager().flush();
        getEntityManager().clear();

        return updatedCount;
    }

    @Override
    public long updateStatusFromReadyToInProgress() {
        long updatedCount = update(challenge)
                .set(challenge.status, ChallengeStatus.IN_PROGRESS)
                .where(challenge.startDate.eq(LocalDate.now()),
                        challenge.status.eq(ChallengeStatus.READY))
                .execute();

        getEntityManager().flush();
        getEntityManager().clear();

        return updatedCount;
    }

    @Override
    public long updateStatusFromInProgressToValidate() {
        long updatedCount = update(challenge)
                .set(challenge.status, ChallengeStatus.VALIDATE)
                .where(challenge.endDate.eq(LocalDate.now()),
                        challenge.status.eq(ChallengeStatus.IN_PROGRESS))
                .execute();

        getEntityManager().flush();
        getEntityManager().clear();

        return updatedCount;
    }

    @Override
    public long updateStatusFromValidateToFinish() {
        long updatedCount = update(challenge)
                .set(challenge.status, ChallengeStatus.FINISH)
                .where(challenge.endDate.eq(LocalDate.now().minusDays(7)),
                        challenge.status.eq(ChallengeStatus.VALIDATE))
                .execute();

        getEntityManager().flush();
        getEntityManager().clear();

        return updatedCount;
    }

    private BooleanBuilder searchCond(ChallengeSearchCondition condition) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        return booleanBuilder
                .and(categoryEq(condition.getCategory()))
                .and(challengeNameContains(condition.getChallengeName()))
                .and(tagNameEq(condition.getTagName()));
    }

    private BooleanExpression challengeNameContains(String challengeName) {
        return hasText(challengeName) ? challenge.name.contains(challengeName) : null;
    }

    private BooleanExpression categoryEq(String category) {
        return hasText(category) ? challenge.category.eq(Category.of(category)) : null;
    }

    public BooleanExpression tagNameEq(String tagName) {
        return hasText(tagName) ? tag.name.eq(tagName) : null;
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
