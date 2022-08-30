package com.challengers.photocheck.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CheckRequest {
    @NotNull
    @Size(min = 1)
    private List<Long> photoCheckIds;
}
