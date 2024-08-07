package org.example.springbootdemo.model.constant;

/**
 * @date 2024/6/16
 **/
public interface ApiConstant {


    String OPEN_PATH = "/open";

    String OPEN_PATH_PATTEN = "/open/**";

    String HEADER_APP_ID = "X-App-Id";

    String HEADER_NONCE = "X-Nonce";

    String HEADER_TIMESTAMP = "X-Timestamp";

    String HEADER_SIGNATURE = "X-Signature";

    String INNER_PATH = "/inner";

    String INNER_PATH_PATTEN = "/inner/**";

    String HEADER_AUTHORIZATION = "Authorization";

    String AUTHORIZATION_BEARER_TOKEN = "Bearer";

}
