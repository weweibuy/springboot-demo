package org.example.springbootdemo.servelt;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.springbootdemo.config.properties.WebJwtProperties;
import org.example.springbootdemo.model.constant.ApiConstant;
import org.example.springbootdemo.model.dto.resp.CommonCodeResponseDTO;
import org.example.springbootdemo.model.eum.ResponseCodeEum;
import org.example.springbootdemo.support.LogTraceContext;
import org.example.springbootdemo.utils.HttpRequestUtils;
import org.example.springbootdemo.utils.JwtUtils;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * @date 2024/7/6
 **/
@Slf4j
@Component
@Order(-90)
@RequiredArgsConstructor
public class AuthorizationFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    private final WebJwtProperties webJwtProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        // 请求是否要验证授权
        boolean matchPath = HttpRequestUtils.isMatchPath(ApiConstant.INNER_PATH_PATTEN, path);
        if (!matchPath) {
            filterChain.doFilter(request, response);
            return;
        }

        /*
         * 在请求头 Authorization中携带token
         * token格式:Bearer {jwt}
         *
         */
        String authorization = request.getHeader(ApiConstant.HEADER_AUTHORIZATION);
        String token = null;
        // token 错误
        if (StringUtils.isBlank(authorization) || authorization.indexOf(ApiConstant.AUTHORIZATION_BEARER_TOKEN + " ") == -1
                || StringUtils.isBlank(token = authorization.substring(ApiConstant.AUTHORIZATION_BEARER_TOKEN.length() + 1))) {
            unauthorized(response, ResponseCodeEum.UNAUTHORIZED.getMsg());
            return;
        }

        boolean success = verifyTokenAndSetUserContext(response, token);
        if (success) {
            filterChain.doFilter(request, response);
        }
    }


    private boolean verifyTokenAndSetUserContext(HttpServletResponse response, String token) throws IOException {
        String username = null;
        try {
            DecodedJWT decodedJWT = JwtUtils.verifyToken(webJwtProperties, token);
            username = decodedJWT.getSubject();
        } catch (TokenExpiredException e) {
            unauthorized(response, "授权已经过期");
            return false;
        } catch (JWTVerificationException e) {
            log.warn("请求token错误", e);
            unauthorized(response, "请求token错误");
            return false;
        } catch (Exception e) {
            log.warn("授权验证失败", e);
            unauthorized(response, "授权验证失败");
            return false;
        }
        LogTraceContext.setUserId(username);
        return true;
    }

    private void unauthorized(HttpServletResponse response, String msg) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setCharacterEncoding("UTF-8");

        CommonCodeResponseDTO codeResponseDTO = CommonCodeResponseDTO.response(ResponseCodeEum.UNAUTHORIZED, msg);


        PrintWriter writer = response.getWriter();
        objectMapper.writeValue(writer, codeResponseDTO);
    }

}
