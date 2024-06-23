package org.example.springbootdemo.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.springbootdemo.config.properties.WebSignProperties;
import org.example.springbootdemo.controller.dto.req.OrderReqDTO;
import org.example.springbootdemo.model.constant.ApiConstant;
import org.example.springbootdemo.model.eum.ResponseCodeEum;
import org.example.springbootdemo.model.exception.BusinessException;
import org.example.springbootdemo.model.vo.ReqSignInfoVO;
import org.example.springbootdemo.model.vo.RespSignInfoVO;
import org.example.springbootdemo.utils.NumCodeGenerateUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.function.Consumer;

/**
 * @date 2024/6/16
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class DemoSignClient {

    private final ObjectMapper objectMapper;

    private final WebSignProperties signProperties;

    private final RestClient restClient = RestClient
            .create();

    private String url = "http://localhost:8080/open/sign-demo?name=tom&age=12";


    public void sendSignReq() throws JsonProcessingException, URISyntaxException {

        OrderReqDTO orderReqDTO = new OrderReqDTO();
        orderReqDTO.setProductId(1L);
        orderReqDTO.setQty(1);
        orderReqDTO.setAmount(new BigDecimal("100"));

        String body = objectMapper.writeValueAsString(orderReqDTO);

        URI reqUri = new URI(url);
        ReqSignInfoVO signInfoVO = buildReqSignInfo(HttpMethod.POST.toString(), reqUri, body);

        // 进行签名, 并构建签名的请求头
        Consumer<HttpHeaders> headersConsumer = signInfoVO.signAndAssembleHeader();

        ResponseEntity<String> response = restClient.method(HttpMethod.POST)
                .uri(reqUri)
                .headers(headersConsumer)
                .body(body)
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(String.class);

        log.info("restClient响应: {}", response);

        verifySign(response);

    }


    private void verifySign(ResponseEntity<String> response) {
        RespSignInfoVO respSignInfoVO = buildRespSignInfo(response);

        boolean b = respSignInfoVO.verifySign();
        if (!b) {
            throw new BusinessException(ResponseCodeEum.BAD_REQUEST_RESP_SIGN.getCode(), "响应签名错误");
        }
    }


    public RespSignInfoVO buildRespSignInfo(ResponseEntity<String> response) {
        int status = response.getStatusCode().value();
        String responseBody = response.getBody();
        HttpHeaders headers = response.getHeaders();
        String timestamp = headers.getFirst(ApiConstant.HEADER_TIMESTAMP);
        String nonce = headers.getFirst(ApiConstant.HEADER_NONCE);
        String signature = headers.getFirst(ApiConstant.HEADER_SIGNATURE);


        if (StringUtils.isBlank(timestamp) || !StringUtils.isNumeric(timestamp)) {
            throw new BusinessException(ResponseCodeEum.BAD_REQUEST_RESP_SIGN.getCode(),
                    "请响应时间戳错误");
        }
        Long reqTimestamp = Long.valueOf(timestamp);
        Long maxReqInterval = 30000L;
        if ((System.currentTimeMillis() - reqTimestamp) > maxReqInterval) {
            throw new BusinessException(ResponseCodeEum.BAD_REQUEST_RESP_SIGN.getCode(),
                    "响应时间与当前时间间隔异常");
        }

        if (StringUtils.isBlank(nonce)) {
            throw new BusinessException(ResponseCodeEum.BAD_REQUEST_RESP_SIGN.getCode(),
                    "未响应nonce");
        }

        if (StringUtils.isBlank(signature)) {
            throw new BusinessException(ResponseCodeEum.BAD_REQUEST_RESP_SIGN.getCode(),
                    "未响应signature");
        }

        RespSignInfoVO respSignInfoVO = new RespSignInfoVO();
        respSignInfoVO.setStatus(status);
        respSignInfoVO.setBody(responseBody);
        respSignInfoVO.setNonce(nonce);
        respSignInfoVO.setTimestamp(reqTimestamp);
        respSignInfoVO.setSignature(signature);
        respSignInfoVO.setServerPublicKeyKey(signProperties.getServerKey().getPublicKey());
        return respSignInfoVO;
    }

    public ReqSignInfoVO buildReqSignInfo(String method, URI uri, String body) {

        String appId = "app_id_001";
        WebSignProperties.ClientKeyProperties clientKeyProperties = signProperties.getClientKey().get(appId);
        ReqSignInfoVO signInfoVO = new ReqSignInfoVO();
        signInfoVO.setAppId(appId);
        signInfoVO.setTimestamp(System.currentTimeMillis() + "");
        signInfoVO.setNonce(NumCodeGenerateUtils.generateNonceStr(32));
        signInfoVO.setPrivateKey(clientKeyProperties.getPrivateKey());

        signInfoVO.setMethod(method);
        signInfoVO.setPath(uri.getPath());
        signInfoVO.setQuery(uri.getQuery());
        signInfoVO.setBody(body);


        return signInfoVO;
    }


}
