package com.rharshit.carsync.repository.model;

import lombok.Data;

@Data
public class CarFilter {
    private Integer limit;
    private Integer skip;
    private String[] cities;
    private String[] makes;
    private String[] models;
    private String[] variants;
    private Integer minYear;
    private Integer maxYear;
    private Integer minPrice;
    private Integer maxPrice;
    private Integer minMileage;
    private Integer maxMileage;
    private Integer minPower;
    private Integer maxPower;
    private Integer minTorque;
    private Integer maxTorque;
    private Integer minLength;
    private Integer maxLength;
    private Integer minWidth;
    private Integer maxWidth;
    private Integer minHeight;
    private Integer maxHeight;
    private Integer minWheelbase;
    private Integer maxWheelbase;
}
