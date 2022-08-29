package com.challengers.photocheck.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CheckRequest {
    @NotNull
    private List<Long> photoCheckIds;
}
