package spring.security.init.global.config.security.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import spring.security.init.global.result.CommonResult;
import spring.security.init.global.result.error.ErrorCode;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.error("Sign-in Fail");

        /**
         * form login 방식
         */
//        String errorMessage = null;
//
//        // 해당계정이 없을때
//        if (exception instanceof UsernameNotFoundException) {
//            errorMessage = ErrorCode.NOT_FOUND_USER.getMessage();
//        }
//
//        // 비밀번호가 틀릴때 BadCredentialsException < AuthenticationException < RuntimeException
//        if (exception instanceof BadCredentialsException) {
//            errorMessage = ErrorCode.NOT_MATCH_PASSWORD.getMessage();
//        }
//
//        errorMessage= URLEncoder.encode(errorMessage,"UTF-8"); // 한글 인코딩 깨지는 문제방지
//        setDefaultFailureUrl("/?error=true&exception=" +errorMessage);
//        onAuthenticationFailure(request, response, exception);

        /**
         * API login 방식
         */
        // 해당계정이 없을때
        if (exception instanceof UsernameNotFoundException) {
            sendErrorResponseApiLogin(response, ErrorCode.NOT_FOUND_USER);
        }

        // 비밀번호가 틀릴때 BadCredentialsException < AuthenticationException < RuntimeException
        if (exception instanceof BadCredentialsException) {
            sendErrorResponseApiLogin(response, ErrorCode.NOT_MATCH_PASSWORD);
        }
    }

    // API login 방식
    private void sendErrorResponseApiLogin(HttpServletResponse response, ErrorCode errorCode) throws HttpMessageNotWritableException, IOException {
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        MediaType jsonMimeType = MediaType.APPLICATION_JSON;

        CommonResult result = CommonResult.createBusinessExceptionResult(errorCode);

        if(jsonConverter.canWrite(result.getClass(), jsonMimeType)) {
            jsonConverter.write(result, jsonMimeType, new ServletServerHttpResponse(response));
        }
    }





}