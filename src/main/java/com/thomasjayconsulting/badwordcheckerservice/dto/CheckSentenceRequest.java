package com.thomasjayconsulting.badwordcheckerservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * API Request Object
 *
 * @author Thomas Jay
 *
 */

@Data
public class CheckSentenceRequest {
    @NotBlank
    @Size(min = 1, max = 2048)
    private String sentence;
}
