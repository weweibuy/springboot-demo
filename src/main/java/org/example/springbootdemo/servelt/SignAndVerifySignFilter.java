package org.example.springbootdemo.servelt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.example.springbootdemo.config.properties.WebSignProperties;
import org.example.springbootdemo.model.constant.ApiConstant;
import org.example.springbootdemo.model.dto.resp.CommonCodeResponseDTO;
import org.example.springbootdemo.model.eum.ResponseCodeEum;
import org.example.springbootdemo.model.vo.ReqSignInfoVO;
import org.example.springbootdemo.model.vo.RespSignInfoVO;
import org.example.springbootdemo.utils.HttpRequestUtils;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

/**
 * 请求日志过滤器执行顺序:  @Order(-103)
 *
 * @date 2024/6/16
 **/
@Component
@Order(-80)
@RequiredArgsConstructor
public class SignAndVerifySignFilter extends OncePerRequestFilter {

    private final WebSignProperties webSignProperties;

    private final ObjectMapper objectMapper;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();

        // 请求是否要验签
        boolean matchPath = HttpRequestUtils.isMatchPath(ApiConstant.OPEN_PATH_PATTEN, path);
        if (!matchPath) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // 校验请求与验签
            String msg = verifyReqAndSign(request);
            if (StringUtils.isNotBlank(msg)) {
                // 验签不通过
                badSignResponse(response, msg);
                return;
            }

            // 执行请求
            filterChain.doFilter(request, response);
        } finally {
            // 响应签名
            responseSign(response);
        }


    }


    public ReqSignInfoVO buildReqSignInfo(HttpServletRequest request) throws IOException {
        return ReqSignInfoVO.fromReq(request,
                appId -> Optional.ofNullable(webSignProperties.getClientKey())
                        .map(m -> m.get(appId))
                        .map(WebSignProperties.ClientKeyProperties::getPublicKey)
                        .orElse(null));
    }

    private String verifyReqAndSign(HttpServletRequest request) throws IOException {
        ReqSignInfoVO signInfoVO = buildReqSignInfo(request);

        String msg = signInfoVO.validate();
        if (StringUtils.isNotBlank(msg)) {
            return msg;
        }

        boolean b = signInfoVO.verifySign();
        if (!b) {
            return "签名错误";
        }
        return null;
    }

    private void badSignResponse(HttpServletResponse response, String msg) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setCharacterEncoding("UTF-8");

        CommonCodeResponseDTO codeResponseDTO = CommonCodeResponseDTO.response(ResponseCodeEum.BAD_REQUEST_SIGN, msg);
        PrintWriter writer = response.getWriter();
        objectMapper.writeValue(writer, codeResponseDTO);
    }


    private void responseSign(HttpServletResponse response) throws IOException {

        if (response instanceof ContentCachingResponseWrapper cachingResponseWrapper) {
            RespSignInfoVO respSignInfoVO = RespSignInfoVO.fromResponse(cachingResponseWrapper, webSignProperties.getServerKey().getPrivateKey());
            // 签名
            respSignInfoVO.sign();

            if (StringUtils.isNotBlank(respSignInfoVO.getSignature())) {
                respSignInfoVO.writeSignHeader(response);
                return;
            }
        }
        serverSignFail(response);
    }


    private void serverSignFail(HttpServletResponse response) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());

        CommonCodeResponseDTO codeResponseDTO = CommonCodeResponseDTO.response(ResponseCodeEum.UNKNOWN_EXCEPTION, "响应签名失败");

        PrintWriter writer = response.getWriter();
        objectMapper.writeValue(writer, codeResponseDTO);
    }


}
