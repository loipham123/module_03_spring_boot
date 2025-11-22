package com.sqc.acedemy.bai_1;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @RequestMapping("/greeting") //API, endpoint
    public String hello(@RequestParam(defaultValue = "Loi") String name,
                        @RequestParam(defaultValue = "Da Nang") String address) {
        return "Hello " + name + ", " + address;
    }
}
