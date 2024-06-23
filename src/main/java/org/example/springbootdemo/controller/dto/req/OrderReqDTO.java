package org.example.springbootdemo.controller.dto.req;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @date 2024/6/16
 **/
@Data
public class OrderReqDTO {

    private Long productId;

    private Integer qty;

    private BigDecimal amount;

}
