package com.challengers.challengetag.domain;

import com.challengers.challenge.domain.Challenge;
import com.challengers.tag.domain.Tag;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChallengeTag {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "challenge_tag_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    public static ChallengeTag associate(Challenge challenge, Tag tag) {
        ChallengeTag cocktailTag = new ChallengeTag();
        cocktailTag.setTag(tag);
        cocktailTag.setChallenge(challenge);

        return cocktailTag;
    }

    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
        challenge.getChallengeTags().addChallengeTag(this);
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }
}
