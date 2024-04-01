package org.example.springbootdemo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
