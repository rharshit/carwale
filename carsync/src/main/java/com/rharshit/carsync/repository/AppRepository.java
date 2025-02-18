package com.rharshit.carsync.repository;

import com.rharshit.carsync.model.CarFilter;
import org.springframework.cache.annotation.Cacheable;

import static com.rharshit.carsync.common.Constants.CACHE_KEY_ALL_CARS;

public interface AppRepository {
    @Cacheable(CACHE_KEY_ALL_CARS)
    CarFilter getCarFilterValues();
}
