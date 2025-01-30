package com.rharshit.carsync.controller;

import com.rharshit.carsync.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/client")
public class ClientController {

    @Autowired
    private CarService carService;

    @PostMapping("/{client}/fetch")
    public String startFetching(@PathVariable String client) {
        return carService.startFetching(client);
    }
}
