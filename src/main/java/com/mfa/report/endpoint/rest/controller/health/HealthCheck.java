package com.mfa.report.endpoint.rest.controller.health;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheck {

    @GetMapping("/ping")
    public String getHealthCheck(){
        return  "pong";
    }
}
