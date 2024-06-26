package org.example.springbootdemo.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.example.springbootdemo.client.DemoSignClient;
import org.example.springbootdemo.model.dto.resp.CommonCodeResponseDTO;
import org.example.springbootdemo.support.validate.annotation.Scale;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

/**
 * @date 2024/3/6
 **/
@RequestMapping("/demo")
@RestController
@Slf4j
@RequiredArgsConstructor
public class DemoController {

    private final DemoSignClient demoSignClient;

    @GetMapping
    public Map<String, String> demo() {
        log.info("demo1 ....");
        log.info("demo2 ....");
        return Collections.singletonMap("hello", "spring boot");
    }

    @PostMapping("/form")
    public void form(HttpServletRequest request, HttpServletResponse response) throws IOException {
        BufferedReader reader = request.getReader();
        PrintWriter writer = response.getWriter();
        IOUtils.copy(reader, writer);
    }

    @PostMapping("/json")
    public Map<String, Object> json(@RequestBody Map<String, Object> req) {
        return req;
    }

    @PostMapping("/date")
    public DemoDateReq date(@RequestBody DemoDateReq demoDateReq) {
        log.info(demoDateReq + "");
        DemoDateReq demoDate = new DemoDateReq();
        demoDate.setDate(new Date());
        demoDate.setLocalDateTime(LocalDateTime.now());
        demoDate.setLocalDate(LocalDate.now());
        demoDate.setLocalTime(LocalTime.now());

        demoDate.setCustomLocalDateTime(LocalDateTime.now());
        demoDate.setCustomDate(new Date());
        return demoDate;
    }


    @GetMapping("/validate")
    public CommonCodeResponseDTO validate(@Valid @Scale(max = 2)
                                          @RequestParam("amount") BigDecimal amount) {
        return CommonCodeResponseDTO.success();
    }


    @GetMapping("/client")
    public CommonCodeResponseDTO client() throws JsonProcessingException, URISyntaxException {
        demoSignClient.sendSignReq();
        return CommonCodeResponseDTO.success();
    }

    @Data
    public static class DemoDateReq {

        private Date date;

        private LocalDateTime localDateTime;

        private LocalDate localDate;

        private LocalTime localTime;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
        private LocalDateTime customLocalDateTime;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
        private Date customDate;


    }


}
