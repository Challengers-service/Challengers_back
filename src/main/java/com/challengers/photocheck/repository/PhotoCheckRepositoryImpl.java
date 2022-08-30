package com.challengers.photocheck.repository;

import com.challengers.photocheck.domain.PhotoCheckStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static com.challengers.photocheck.domain.QPhotoCheck.*;

@Repository
public class PhotoCheckRepositoryImpl implements PhotoCheckRepositoryCustom{

    private JPAQueryFactory queryFactory;
    private EntityManager em;

    public PhotoCheckRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
        this.em = em;
    }

    @Override
    public void updateStatusByIds(List<Long> photoCheckIds, PhotoCheckStatus photoCheckStatus) {
        queryFactory.update(photoCheck)
                .set(photoCheck.status, photoCheckStatus)
                .where(photoCheck.id.in(photoCheckIds))
                .execute();

        em.flush();
        em.clear();
    }
}
