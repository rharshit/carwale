package com.rharshit.carsync.repository;

import com.rharshit.carsync.model.CarFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;

import java.util.List;

import static com.rharshit.carsync.common.Constants.CACHE_KEY_ALL_CARS;

public class AppRepositoryImpl implements AppRepository {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    @Cacheable(CACHE_KEY_ALL_CARS)
    public CarFilter getCarFilterValues() {
        GroupOperation year = Aggregation.group()
                .min("$year").as("minYear")
                .max("$year").as("maxYear")
                .min("$price").as("minPrice")
                .max("$price").as("maxPrice")
                .min("$mileage").as("minMileage")
                .max("$mileage").as("maxMileage")
                .min("$specs.enginePower").as("minPower")
                .max("$specs.enginePower").as("maxPower")
                .min("$specs.engineTorque").as("minTorque")
                .max("$specs.engineTorque").as("maxTorque")
                .min("$specs.length").as("minLength")
                .max("$specs.length").as("maxLength")
                .min("$specs.width").as("minWidth")
                .max("$specs.width").as("maxWidth")
                .min("$specs.height").as("minHeight")
                .max("$specs.height").as("maxHeight")
                .min("$specs.wheelbase").as("minWheelbase")
                .max("$specs.wheelbase").as("maxWheelbase");
        Aggregation aggregation = Aggregation.newAggregation(year);
        List<CarFilter> response = mongoTemplate.aggregate(aggregation, "carModels", CarFilter.class).getMappedResults();
        return response.getFirst();
    }
}
