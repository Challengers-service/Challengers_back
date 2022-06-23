package com.challengers.tag.dto;

import com.challengers.tag.domain.Tag;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class TagResponse {
    private Long id;
    private String name;

    public static List<TagResponse> listOf(List<Tag> tags) {
        return tags.stream()
                .map(TagResponse::of)
                .collect(Collectors.toList());
    }

    private static TagResponse of(Tag tag) {
        return new TagResponse(tag.getId(), tag.getName());
    }
}
