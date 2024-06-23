package org.example.springbootdemo.model.vo;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.example.springbootdemo.model.constant.ApiConstant;
import org.example.springbootdemo.utils.NumCodeGenerateUtils;
import org.example.springbootdemo.utils.RSAUtils;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @date 2024/6/16
 **/
@Slf4j
@Data
public class RespSignInfoVO {

    private Integer status;

    private Long timestamp;

    private String body;

    private String nonce;

    private PrivateKey serverPrivateKey;

    private PublicKey serverPublicKeyKey;

    private String signData;

    private String signature;

    public static RespSignInfoVO fromResponse(ContentCachingResponseWrapper cachingResponseWrapper, PrivateKey privateKey) throws IOException {
        int status = cachingResponseWrapper.getStatus();
        InputStream contentInputStream = cachingResponseWrapper.getContentInputStream();
        String body = IOUtils.toString(contentInputStream, StandardCharsets.UTF_8);

        RespSignInfoVO respSignInfoVO = new RespSignInfoVO();
        respSignInfoVO.status = status;
        respSignInfoVO.timestamp = System.currentTimeMillis();
        respSignInfoVO.body = Optional.ofNullable(body)
                .orElse("");

        int nonceLen = 32;
        respSignInfoVO.nonce = NumCodeGenerateUtils.generateNonceStr(nonceLen);
        respSignInfoVO.serverPrivateKey = privateKey;

        return respSignInfoVO;

    }


    private void buildSignData() {
        this.signData = Stream.of(status + "", timestamp + "", nonce, body)
                .collect(Collectors.joining(","));

    }


    public void sign() {
        buildSignData();
        this.signature = RSAUtils.signToHexUseSHA256withRSA(serverPrivateKey, signData);
    }

    public void writeSignHeader(HttpServletResponse response) {
        response.setHeader(ApiConstant.HEADER_NONCE, nonce);
        response.setHeader(ApiConstant.HEADER_TIMESTAMP, timestamp + "");
        response.setHeader(ApiConstant.HEADER_SIGNATURE, signature);
    }

    public boolean verifySign() {
        buildSignData();
        return RSAUtils.verifyHexSignUseSHA256withRSA(serverPublicKeyKey, signData, signature);
    }
}
