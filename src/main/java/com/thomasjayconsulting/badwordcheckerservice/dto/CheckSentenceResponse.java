package com.thomasjayconsulting.badwordcheckerservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Set;

@Data
public class CheckSentenceResponse {
    @NotBlank
    private String status;

    @NotBlank
    private String messages;

    private int count;

    private Set badWords;
}
