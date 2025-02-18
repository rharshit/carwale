package com.rharshit.carsync.model;

import lombok.Data;

import java.util.List;

@Data
public class AllCarsResponse extends ResponseModel {
    private int total;
    private int length;
    private List<CarModel> cars;
    private boolean loadMore;
}
