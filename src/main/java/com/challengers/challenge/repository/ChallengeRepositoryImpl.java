package com.challengers.challenge.repository;

import com.challengers.cart.domain.QCart;
import com.challengers.challenge.domain.Category;
import com.challengers.challenge.domain.Challenge;
import com.challengers.challenge.domain.ChallengeStatus;
import com.challengers.challenge.domain.CheckFrequencyType;
import com.challengers.challenge.dto.ChallengeSearchCondition;
import com.challengers.common.Querydsl4RepositorySupport;
import com.challengers.userchallenge.ChallengeJoinManager;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;

import static com.challengers.cart.domain.QCart.*;
import static com.challengers.challenge.domain.QChallenge.*;
import static com.challengers.challengetag.domain.QChallengeTag.challengeTag;
import static com.challengers.tag.domain.QTag.*;
import static com.challengers.userchallenge.domain.QUserChallenge.*;
import static org.springframework.util.StringUtils.hasText;

public class ChallengeRepositoryImpl extends Querydsl4RepositorySupport implements ChallengeRepositoryCustom {

    public ChallengeRepositoryImpl() {
        super(Challenge.class);
    }

    @Override
    public List<Challenge> search(ChallengeSearchCondition condition, Pageable pageable) {
        JPAQuery<Challenge> preQuery = getQueryFactory().selectFrom(challenge)
                .where(challenge.status.in(ChallengeStatus.READY, ChallengeStatus.IN_PROGRESS));
        return preQuery.where(searchCond(condition, preQuery))
                .orderBy(challengeSort(pageable, preQuery))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();
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

    private BooleanBuilder searchCond(ChallengeSearchCondition condition, JPAQuery<Challenge> query) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        return booleanBuilder
                .and(categoryEq(condition.getCategory()))
                .and(challengeNameContains(condition.getChallengeName()))
                .and(tagNameEq(condition.getTagName(), query));
    }

    private BooleanExpression challengeNameContains(String challengeName) {
        return hasText(challengeName) ? challenge.name.contains(challengeName) : null;
    }

    private BooleanExpression categoryEq(String category) {
        return hasText(category) ? challenge.category.eq(Category.of(category)) : null;
    }

    public BooleanExpression tagNameEq(String tagName, JPAQuery<Challenge> query) {
        query.leftJoin(challenge.challengeTags.challengeTags, challengeTag)
                .leftJoin(challengeTag.tag, tag);
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

    private OrderSpecifier<?> challengeSort(Pageable page, JPAQuery<Challenge> contentQuery) {
        if (!page.getSort().isEmpty()) {
            for (Sort.Order order : page.getSort()) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                switch (order.getProperty()){
                    case "userCount":
                        contentQuery.join(userChallenge).on(challenge.id.eq(userChallenge.challenge.id));
                        return new OrderSpecifier(direction, userChallenge.id.count());
                    case "id":
                        return new OrderSpecifier(direction, challenge.id);
                    case "cart":
                        contentQuery.leftJoin(cart).on(cart.challenge.id.eq(challenge.id));
                        return new OrderSpecifier<>(direction, cart.id);
                }
            }
        }
        return new OrderSpecifier(Order.DESC, challenge.id);
    }

}
