package com.sns.api.posts.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class PostCreateRequestDto {

    @NotBlank
    @Size(max = 500)
    private String content;

}
