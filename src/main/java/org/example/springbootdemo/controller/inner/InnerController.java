package org.example.springbootdemo.controller.inner;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.springbootdemo.model.constant.ApiConstant;
import org.example.springbootdemo.model.dto.resp.CommonCodeResponseDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @date 2024/7/6
 **/
@Slf4j
@RestController
@RequestMapping(ApiConstant.INNER_PATH)
@RequiredArgsConstructor
public class InnerController {

    @GetMapping("/demo")
    public CommonCodeResponseDTO signDemo() {
        return CommonCodeResponseDTO.success();
    }


}
