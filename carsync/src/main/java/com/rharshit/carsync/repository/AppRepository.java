package com.rharshit.carsync.repository;

import com.rharshit.carsync.model.CarFilter;
import org.springframework.cache.annotation.Cacheable;

public interface AppRepository {
    @Cacheable("allCars")
    CarFilter getCarFilterValues();
}
