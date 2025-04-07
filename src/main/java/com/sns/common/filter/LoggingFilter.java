package com.sns.common.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Enumeration;

@Slf4j
public class LoggingFilter implements Filter {

    /**
     * 필터 체인에서 요청이 들어올 때마다 호출되는 메소드.
     * 이 메소드에서는 HTTP 요청에 대한 기본 정보, 헤더, 파라미터를 로그로 출력합니다.
     *
     * @param request  클라이언트의 요청 객체
     * @param response 클라이언트에 전송될 응답 객체
     * @param chain    다음 필터나 서블릿으로 요청을 전달하는 체인
     * @throws IOException      입출력 관련 예외
     * @throws ServletException 서블릿 관련 예외
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        // ServletRequest를 HttpServletRequest로 캐스팅하여 HTTP 요청 관련 메소드를 사용
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        // 요청 URI를 가져옴
        String requestURI = httpRequest.getRequestURI();

        // 요청 정보 로그 출력
        log.info("=============== Request Info ===============");
        log.info("Request URI: {}", requestURI);
        log.info("Method: {}", httpRequest.getMethod());
        log.info("Remote Addr: {}", httpRequest.getRemoteAddr());

        // 요청 헤더 정보를 열거하여 하나씩 로그로 출력
        Enumeration<String> headerNames = httpRequest.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String header = headerNames.nextElement();
            log.info("Header [{}]: {}", header, httpRequest.getHeader(header));
        }

        // 요청 파라미터 정보를 열거하여 하나씩 로그로 출력
        Enumeration<String> paramNames = httpRequest.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            String[] paramValues = httpRequest.getParameterValues(paramName);
            for (String value : paramValues) {
                log.info("Parameter [{}]: {}", paramName, value);
            }
        }
        log.info("============================================");

        // 현재 필터의 작업이 끝났으므로, 다음 필터 또는 최종 서블릿으로 요청을 전달
        chain.doFilter(request, response);
    }
}
