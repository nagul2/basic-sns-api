package com.sns.common.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sns.common.component.BaseResponse;
import com.sns.common.component.Const;
import com.sns.common.component.ResultCode;
import com.sns.common.exception.CustomException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.AntPathMatcher;

import java.io.IOException;

@Slf4j
public class LoginFilter implements Filter {

    // 인증을 하지 않아도 되는 URL 패턴 목록 (화이트 리스트)
    private static final String[] WHITE_LIST = {"/", "/auth/**"};

    /**
     * 모든 요청에 대해 로그인 여부를 체크하는 필터 메소드.
     * 화이트 리스트에 포함되지 않은 경우에만 로그인 상태를 확인하며,
     * 로그인되지 않은 경우 CustomException을 발생시켜 예외 처리 메소드에서 JSON 응답을 직접 작성함.
     *
     * @param request  클라이언트 요청 정보
     * @param response 클라이언트 응답 정보
     * @param chain    다음 필터 또는 서블릿으로 요청을 전달하는 필터 체인
     * @throws IOException      입출력 관련 예외
     * @throws ServletException 서블릿 관련 예외
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // ServletRequest를 HttpServletRequest로 캐스팅하여 HTTP 관련 메소드 사용
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String requestURI = httpRequest.getRequestURI();

        log.info("로그인 필터 로직 실행");

        try {
            // 요청 URI가 화이트 리스트에 해당하지 않는 경우에만 로그인 상태 체크
            if (!isWhiteList(requestURI)) {
                // 세션이 없거나 로그인 사용자 정보가 없으면 인증 실패로 간주
                if (httpRequest.getSession(false) == null ||
                        httpRequest.getSession(false).getAttribute(Const.LOGIN_USER) == null) {
                    throw new CustomException(ResultCode.AUTHENTICATION_FAILED);
                }
            }

            // 로그인 체크 통과 시, 필터 체인을 통해 다음 필터 또는 서블릿/컨트롤러로 요청 전달
            chain.doFilter(request, response);
        } catch (CustomException ex) {
            // 로그인 인증 실패 등 CustomException 발생 시 직접 예외 처리 및 JSON 응답 작성
            handleCustomException(httpRequest, httpResponse, ex);
        }
    }

    /**
     * 요청된 URL이 화이트 리스트에 포함되어 있는지 확인하는 메서드.
     *
     * @param requestURI 클라이언트 요청 URI
     * @return 화이트 리스트에 포함되면 true, 아니면 false
     */
    private boolean isWhiteList(String requestURI) {
        // AntPathMatcher를 사용하여 패턴 매칭 수행
        AntPathMatcher matcher = new AntPathMatcher();
        for (String pattern : WHITE_LIST) {
            if (matcher.match(pattern, requestURI)) {
                return true;
            }
        }
        return false;
    }

    /**
     * CustomException을 처리하여 JSON 형태의 응답을 직접 작성하는 메서드.
     * ObjectMapper를 사용하여 BaseResponse 객체를 JSON 문자열로 변환한 후 응답 본문에 작성.
     *
     * @param request  클라이언트 요청 정보
     * @param response 클라이언트 응답 정보
     * @param ex       발생한 CustomException
     * @throws IOException 입출력 관련 예외
     */
    private void handleCustomException(HttpServletRequest request, HttpServletResponse response, CustomException ex)
            throws IOException {
        // 예외 발생 URI와 예외 메시지를 로그에 기록
        log.error("Exception URI : " + request.getRequestURI());
        log.error("customException : " + ex.getMessage(), ex);

        // ObjectMapper 설정: JavaTimeModule 등록 및 날짜 형식 설정
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // 인증 실패 응답 생성 (data는 빈 값으로 처리)
        BaseResponse res = BaseResponse.error(ResultCode.AUTHENTICATION_FAILED, StringUtils.EMPTY);

        // BaseResponse 객체를 JSON 문자열로 변환
        String responseBody = objectMapper.writeValueAsString(res);

        // 응답 설정: JSON Content-Type, 상태 코드 UNAUTHORIZED (401), UTF-8 인코딩
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(responseBody);
    }
}
