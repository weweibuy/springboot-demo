package org.example.springbootdemo.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Map;

/**
 * @date 2024/3/6
 **/
@RequestMapping("/demo")
@RestController
@Slf4j
public class DemoController {

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


}
