package com.rharshit.carsync.repository;

import com.rharshit.carsync.model.CarFilter;
import com.rharshit.carsync.model.CarModel;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

import static com.rharshit.carsync.common.Constants.CACHE_KEY_ALL_CARS;

public interface AppRepository {
    @Cacheable(CACHE_KEY_ALL_CARS)
    CarFilter getCarFilterValues();

    List<String> getAllCities();

    @Cacheable(CACHE_KEY_ALL_CARS)
    List<CarModel> findByFilters(CarFilter carFilter);
}
