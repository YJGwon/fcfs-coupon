package com.coupop.fcfscoupon.log;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class TestController {

    @GetMapping("/get")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Response> get() {
        return ResponseEntity.ok(new Response("value"));
    }

    public record Response(String value) {
    }
}
