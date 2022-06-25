package com.challengers.follow.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@NoArgsConstructor
@Entity
@Getter
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"to_user", "from_user"})
)
@IdClass(Follow.PK.class)
public class Follow {

    @Id
    @Column(name = "to_user", insertable = false, updatable = false)
    private Long toUser;

    @Id
    @Column(name = "from_user", insertable = false, updatable = false)
    private Long fromUser;

    public Follow(Long toUser, Long fromUser) {
        this.toUser = toUser;
        this.fromUser = fromUser;
    }

    public static class PK implements Serializable {
        Long toUser;
        Long fromUser;
    }
}
