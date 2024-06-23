package org.example.springbootdemo.controller.open;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.springbootdemo.controller.dto.req.OrderReqDTO;
import org.example.springbootdemo.controller.dto.resp.OrderRespDTO;
import org.example.springbootdemo.model.constant.ApiConstant;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @date 2024/6/16
 **/
@Slf4j
@RestController
@RequestMapping(ApiConstant.OPEN_PATH)
@RequiredArgsConstructor
public class OpenController {


    @PostMapping("/sign-demo")
    public OrderRespDTO signDemo(@RequestBody OrderReqDTO orderReq) {
        OrderRespDTO orderRespDTO = new OrderRespDTO();
        orderRespDTO.setOrderId(1L);
        return orderRespDTO;
    }


}
