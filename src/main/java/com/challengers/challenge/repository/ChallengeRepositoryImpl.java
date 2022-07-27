package com.challengers.challenge.repository;

import com.challengers.challenge.domain.Category;
import com.challengers.challenge.domain.Challenge;
import com.challengers.challenge.domain.ChallengeStatus;
import com.challengers.challenge.dto.ChallengeSearchCondition;
import com.challengers.common.Querydsl4RepositorySupport;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;

import static com.challengers.challenge.domain.QChallenge.*;
import static com.challengers.challengetag.domain.QChallengeTag.challengeTag;
import static com.challengers.tag.domain.QTag.*;
import static org.springframework.util.StringUtils.hasText;

public class ChallengeRepositoryImpl extends Querydsl4RepositorySupport implements ChallengeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ChallengeRepositoryImpl(EntityManager em) {
        super(Challenge.class);
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Challenge> search(ChallengeSearchCondition condition, Pageable pageable) {
        return applyPagination(pageable, contentQuery -> contentQuery
                        .select(challenge).distinct()
                        .from(challenge)
                        .join(challenge.challengeTags.challengeTags, challengeTag)
                        .join(challengeTag.tag, tag)
                        .where(searchCond(condition)
                                .and(challenge.status.in(ChallengeStatus.READY, ChallengeStatus.IN_PROGRESS))),

                countQuery -> countQuery
                        .select(challenge).distinct()
                        .from(challenge)
                        .join(challenge.challengeTags.challengeTags, challengeTag)
                        .join(challengeTag.tag, tag)
                        .where(searchCond(condition)
                                .and(challenge.status.in(ChallengeStatus.READY, ChallengeStatus.IN_PROGRESS)))
        );
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
}
