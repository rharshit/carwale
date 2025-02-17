package com.rharshit.carsync.controller;

import com.rharshit.carsync.model.AllCarsResponse;
import com.rharshit.carsync.model.CarFilter;
import com.rharshit.carsync.model.CarModel;
import com.rharshit.carsync.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/car")
public class CarController {

    @Autowired
    private CarService service;

    @PostMapping
    public AllCarsResponse getCars(@RequestBody CarFilter carFilter) {
        return service.getCars(carFilter);
    }

    @PostMapping("/add")
    public boolean addCar(@RequestBody List<CarModel> carModels) {
        return service.addCar(carModels);
    }
}
