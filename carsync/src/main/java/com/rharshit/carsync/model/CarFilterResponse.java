package com.rharshit.carsync.model;

import lombok.Data;

import java.util.List;

@Data
public class CarFilterResponse extends ResponseModel {
    List<String> cities;
    List<MakeModel> makeModels;
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

    public void setValues(CarFilter carFilter) {
        setMinYear(carFilter.getMinYear());
        setMaxYear(carFilter.getMaxYear());
        setMinPrice(carFilter.getMinPrice());
        setMaxPrice(carFilter.getMaxPrice());
        setMinMileage(carFilter.getMinMileage());
        setMaxMileage(carFilter.getMaxMileage());
        setMinPower(carFilter.getMinPower());
        setMaxPower(carFilter.getMaxPower());
        setMinTorque(carFilter.getMinTorque());
        setMaxTorque(carFilter.getMaxTorque());
        setMinLength(carFilter.getMinLength());
        setMaxLength(carFilter.getMaxLength());
        setMinWidth(carFilter.getMinWidth());
        setMaxWidth(carFilter.getMaxWidth());
        setMinHeight(carFilter.getMinHeight());
        setMaxHeight(carFilter.getMaxHeight());
        setMinWheelbase(carFilter.getMinWheelbase());
        setMaxWheelbase(carFilter.getMaxWheelbase());
    }
}
