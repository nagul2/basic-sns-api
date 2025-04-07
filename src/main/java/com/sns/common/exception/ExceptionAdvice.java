package com.sns.common.exception;

import com.sns.common.component.BaseResponse;
import com.sns.common.component.ResultCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;

@Slf4j
@ControllerAdvice // 전역 예외 처리를 위한 어노테이션
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE) // 모든 컨트롤러에서 JSON 응답을 반환하도록 설정
public class ExceptionAdvice {

	/**
	 * CustomException 처리 핸들러.
	 * CustomException 발생 시, 해당 예외의 ResultCode에 맞춰 응답을 생성.
	 *
	 * @param request HTTP 요청 정보
	 * @param e 발생한 CustomException
	 * @return ResponseEntity에 담긴 BaseResponse 객체
	 */
	@ExceptionHandler(CustomException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Object handleCustomException(HttpServletRequest request, CustomException e) {
		// 예외 발생 정보 로깅
		this.logError(request, "customException", e);
		// 예외에 설정된 상태 코드를 기반으로 응답 생성
		return ResponseEntity
				.status(e.getResultCode().getStatus())
				.body(BaseResponse.error(e.getResultCode(), e));
	}

	/**
	 * HttpClientErrorException, MissingServletRequestParameterException, NoResourceFoundException 처리 핸들러.
	 * 해당 예외 발생 시, NOT_FOUND(404) 상태와 함께 응답을 반환.
	 *
	 * @param request HTTP 요청 정보
	 * @param e 발생한 예외
	 * @return ResponseEntity에 담긴 BaseResponse 객체
	 */
	@ExceptionHandler({
			HttpClientErrorException.class,
			MissingServletRequestParameterException.class,
			NoResourceFoundException.class
	})
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public Object handleNotFoundExceptions(HttpServletRequest request, Exception e) {
		// 예외 발생 정보 로깅
		this.logError(request, "NotFound", e);
		// 404 상태로 응답 생성
		return ResponseEntity
				.status(HttpStatus.NOT_FOUND)
				.body(BaseResponse.error(ResultCode.NOT_FOUND, ResultCode.NOT_FOUND.getDefaultMessage()));
	}

	/**
	 * DataAccessException 처리 핸들러.
	 * 데이터베이스 관련 예외 발생 시, 내부 서버 오류(500)와 함께 응답을 반환.
	 *
	 * @param request HTTP 요청 정보
	 * @param e 발생한 DataAccessException
	 * @return ResponseEntity에 담긴 BaseResponse 객체
	 */
	@ExceptionHandler(DataAccessException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Object handleDataAccessException(HttpServletRequest request, DataAccessException e) {
		// 예외 발생 정보 로깅
		this.logError(request, "DataAccessException", e);
		// 데이터베이스 실패 관련 상태 코드로 응답 생성
		return ResponseEntity
				.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(BaseResponse.error(ResultCode.DB_FAIL, ResultCode.DB_FAIL.getDefaultMessage()));
	}

	/**
	 * MethodArgumentNotValidException 처리 핸들러.
	 * 유효성 검증 실패 시, BAD_REQUEST(400) 상태와 함께 응답을 반환.
	 *
	 * @param request HTTP 요청 정보
	 * @param e 발생한 MethodArgumentNotValidException
	 * @return ResponseEntity에 담긴 BaseResponse 객체
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Object handleValidationException(HttpServletRequest request, MethodArgumentNotValidException e) {
		// 예외 발생 정보 로깅
		this.logError(request, "MethodArgumentNotValidException", e);

		// 유효성 검증 실패한 필드 정보 추출
		List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
		// 에러 메시지 추출, 필드 에러가 없으면 기본 메시지 사용
		String message = fieldErrors.isEmpty() ? ResultCode.VALID_FAIL.getDefaultMessage() :
				fieldErrors.get(fieldErrors.size() - 1).getDefaultMessage();

		// BAD_REQUEST 상태로 응답 생성
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(BaseResponse.error(ResultCode.VALID_FAIL, message));
	}

	/**
	 * 기타 모든 예외 처리 핸들러.
	 * 예상치 못한 예외 발생 시, INTERNAL_SERVER_ERROR(500) 상태와 함께 응답을 반환.
	 *
	 * @param request HTTP 요청 정보
	 * @param e 발생한 Exception
	 */
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Object handleGenericException(HttpServletRequest request, Exception e) {
		// 예외 발생 정보 로깅
		this.logError(request, "Exception", e);
		// 일반적인 실패 상태 코드로 응답 생성
		return ResponseEntity
				.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(BaseResponse.error(ResultCode.FAIL, ResultCode.FAIL.getDefaultMessage()));
	}

	/**
	 * 예외 발생 시, 로그를 기록하는 메소드.
	 *
	 * @param request HTTP 요청 정보
	 * @param label 예외 유형 라벨
	 * @param e 발생한 Exception
	 */
	private void logError(HttpServletRequest request, String label, Exception e) {
		// 요청 URI 로깅
		log.error("Exception URI : {}", request.getRequestURI());
		// 예외 메시지와 스택 트레이스 로깅
		log.error("{} : {}", label, e.getMessage(), e);
	}
}