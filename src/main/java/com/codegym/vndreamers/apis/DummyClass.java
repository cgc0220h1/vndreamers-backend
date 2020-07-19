package com.codegym.vndreamers.apis;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
public class DummyClass {
    @GetMapping
    @ResponseBody
    public String hello() {
        return "Hello World";
    }
}
