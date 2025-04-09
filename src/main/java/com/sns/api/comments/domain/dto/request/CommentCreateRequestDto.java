package com.sns.api.comments.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentCreateRequestDto {

    @NotBlank(message = "댓글 내용이 없습니다.")
    @Size(max = 500, message = "댓글은 최대 500글자까지 입력 가능합니다.")
    private String content;

}
