package com.rharshit.carsync.controller;

import com.rharshit.carsync.repository.model.CarModel;
import com.rharshit.carsync.service.CoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/car")
public class CarController {

    @Autowired
    private CoreService service;

    @GetMapping
    public List<CarModel> getCars() {
        return service.getCars();
    }
}
