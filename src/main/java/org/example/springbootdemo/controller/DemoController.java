package org.example.springbootdemo.controller;

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
public class DemoController {

    @GetMapping
    public Map<String, String> demo() {
        return Collections.singletonMap("hello", "spring boot");
    }


}
