package org.example.springbootdemo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.springbootdemo.config.properties.WebRSAProperties;
import org.example.springbootdemo.utils.ClassPathFileUtils;
import org.example.springbootdemo.utils.RSAUtils;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidKeyException;

/**
 * @date 2024/6/1
 **/
@Slf4j
@RestController
@RequestMapping("/codec")
@RequiredArgsConstructor
public class CodecController {

    private final WebRSAProperties webRSAProperties;

    @GetMapping("/rsa-public-key")
    public String fetchRsaPublicKey() {
        String publicKeyPath = webRSAProperties.getPublicKeyPath();
        return ClassPathFileUtils.classPathFileContentOrOther(publicKeyPath);
    }


    @GetMapping("/rsa-encode")
    public String rsaEncode(@RequestParam("data") String data) throws InvalidKeyException {
        return RSAUtils.encryptToHex(webRSAProperties.getPublicKey(), data);
    }

}
