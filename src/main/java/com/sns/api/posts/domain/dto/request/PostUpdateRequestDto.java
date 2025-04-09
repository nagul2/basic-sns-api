package com.sns.api.posts.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class PostUpdateRequestDto {

    @NotBlank(message = "본문을 입력해주세요.")
    @Size(max = 2000, message = "게시물 본문은 2000자까지만 입력 가능합니다.")
    private String content;

}
