package com.rharshit.carsync.controller;

import com.rharshit.carsync.service.CoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CoreController {

    @Autowired
    private CoreService coreService;

    @GetMapping("/test")
    public String test() {
        return coreService.test();
    }
}
