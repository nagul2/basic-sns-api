package com.sns.common.exception;

import com.sns.common.component.ResultCode;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

	private final ResultCode resultCode;

    public CustomException(ResultCode resultCode) {
        this.resultCode = resultCode;
    }

    public CustomException(ResultCode resultCode, String message) {
        super(message);
        this.resultCode = resultCode;
    }
}