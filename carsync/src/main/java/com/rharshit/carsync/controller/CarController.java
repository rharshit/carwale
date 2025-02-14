package com.rharshit.carsync.controller;

import com.rharshit.carsync.repository.model.CarModel;
import com.rharshit.carsync.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/car")
public class CarController {

    @Autowired
    private CarService service;

    @GetMapping
    public List<CarModel> getCars() {
        return service.getCars();
    }

    @PostMapping
    public boolean addCar(@RequestBody List<CarModel> carModels) {
        return service.addCar(carModels);
    }
}
