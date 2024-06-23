package org.example.springbootdemo.model.vo;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.example.springbootdemo.model.constant.ApiConstant;
import org.example.springbootdemo.utils.RSAUtils;
import org.springframework.http.HttpHeaders;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @date 2024/6/16
 **/
@Data
public class ReqSignInfoVO {

    private String path;

    private String method;

    private String query;

    private String appId;

    private String nonce;

    private String timestamp;

    private String body;

    private String signature;

    private PublicKey clientPublicKey;

    private String signData;

    private PrivateKey privateKey;

    public static ReqSignInfoVO fromReq(HttpServletRequest request, Function<String, PublicKey> publicKeyFunction) throws IOException {
        ReqSignInfoVO signInfo = new ReqSignInfoVO();

        signInfo.method = request.getMethod().toUpperCase();
        signInfo.path = Optional.ofNullable(request.getRequestURI())
                .orElse("");
        signInfo.query = Optional.ofNullable(request.getQueryString())
                .orElse("");

        signInfo.appId = request.getHeader(ApiConstant.HEADER_APP_ID);
        signInfo.nonce = request.getHeader(ApiConstant.HEADER_NONCE);
        signInfo.timestamp = request.getHeader(ApiConstant.HEADER_TIMESTAMP);
        signInfo.body = Optional.ofNullable(IOUtils.toString(request.getInputStream(), StandardCharsets.UTF_8))
                .orElse("");
        signInfo.signature = request.getHeader(ApiConstant.HEADER_SIGNATURE);

        // 接入方公钥
        signInfo.clientPublicKey = publicKeyFunction.apply(signInfo.appId);

        return signInfo;
    }


    private void buildSignData() {
        this.signData = Stream.of(path, query, method, timestamp, nonce, body)
                .collect(Collectors.joining(","));
    }

    public String validate() {
        if (StringUtils.isBlank(appId)) {
            return "未上传appId";
        }
        if (StringUtils.isBlank(timestamp) || !StringUtils.isNumeric(timestamp)) {
            return "请求时间戳错误";
        }
        Long reqTimestamp = Long.valueOf(timestamp);
        Long maxReqInterval = 30000L;
        if ((System.currentTimeMillis() - reqTimestamp) > maxReqInterval) {
            return "请求时间与当前时间间隔异常";
        }

        if (StringUtils.isBlank(nonce)) {
            return "未上传nonce";
        }

        if (StringUtils.isBlank(signature)) {
            return "未上传signature";
        }
        if (clientPublicKey == null) {
            return "未配置公钥";
        }
        return null;
    }


    public boolean verifySign() {
        buildSignData();
        return RSAUtils.verifyHexSignUseSHA256withRSA(clientPublicKey, signData, signature);
    }


    public Consumer<HttpHeaders> signAndAssembleHeader() {
        buildSignData();

        signature = RSAUtils.signToHexUseSHA256withRSA(privateKey, signData);

        return h -> {
            h.add(ApiConstant.HEADER_APP_ID, appId);
            h.add(ApiConstant.HEADER_NONCE, nonce);
            h.add(ApiConstant.HEADER_TIMESTAMP, timestamp);
            h.add(ApiConstant.HEADER_SIGNATURE, signature);
        };

    }
}
