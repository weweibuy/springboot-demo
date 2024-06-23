package org.example.springbootdemo.model.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.springbootdemo.model.eum.ResponseCodeEum;
import org.example.springbootdemo.mvc.ResponseCodeAndMsg;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonCodeResponseDTO implements ResponseCodeAndMsg {

    private String code;

    private String msg;

    public CommonCodeResponseDTO(ResponseCodeAndMsg responseCodeAndMsg) {
        this.code = responseCodeAndMsg.getCode();
        this.msg = responseCodeAndMsg.getMsg();
    }

    public static CommonCodeResponseDTO success() {
        return new CommonCodeResponseDTO(ResponseCodeEum.SUCCESS);
    }

    public static CommonCodeResponseDTO response(ResponseCodeAndMsg responseCodeAndMsg) {
        return new CommonCodeResponseDTO(responseCodeAndMsg);
    }

    public static CommonCodeResponseDTO response(String code, String msg) {
        return new CommonCodeResponseDTO(code, msg);
    }


    public static CommonCodeResponseDTO response(ResponseCodeAndMsg responseCodeAndMsg, String msg) {
        return new CommonCodeResponseDTO(responseCodeAndMsg.getCode(), msg);
    }

}