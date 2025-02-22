package com.rharshit.carsync.repository;

import com.rharshit.carsync.model.CarFilter;
import com.rharshit.carsync.model.CarModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
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

    @Override
    public List<String> getAllCities() {
        return mongoTemplate
                .getCollection("carModels")
                .distinct("city", String.class)
                .into(new ArrayList<>());
    }

    @Override
    @Cacheable(CACHE_KEY_ALL_CARS)
    public List<CarModel> findByFilters(CarFilter carFilter) {
        List<Criteria> criteria = new ArrayList<>();
        criteria.add(Criteria.where("_id").exists(true));
        if (carFilter.getCities() != null && carFilter.getCities().length > 0) {
            criteria.add(Criteria.where("city").in((Object[]) carFilter.getCities()));
        }
        if (carFilter.getMakes() != null && carFilter.getMakes().length > 0) {
            criteria.add(Criteria.where("make").in((Object[]) carFilter.getMakes()));
        }
        if (carFilter.getModels() != null && carFilter.getModels().length > 0) {
            criteria.add(Criteria.where("model").in((Object[]) carFilter.getModels()));
        }
        if (carFilter.getVariants() != null && carFilter.getVariants().length > 0) {
            criteria.add(Criteria.where("variant").in((Object[]) carFilter.getVariants()));
        }
        if (carFilter.getMinYear() != null) {
            criteria.add(Criteria.where("year").gte(carFilter.getMinYear()));
        }
        if (carFilter.getMaxYear() != null) {
            criteria.add(Criteria.where("year").lte(carFilter.getMaxYear()));
        }
        if (carFilter.getMinPrice() != null) {
            criteria.add(Criteria.where("price").gte(carFilter.getMinPrice()));
        }
        if (carFilter.getMaxPrice() != null) {
            criteria.add(Criteria.where("price").lte(carFilter.getMaxPrice()));
        }
        if (carFilter.getMinMileage() != null) {
            criteria.add(Criteria.where("mileage").gte(carFilter.getMinMileage()));
        }
        if (carFilter.getMaxMileage() != null) {
            criteria.add(Criteria.where("   mileage").lte(carFilter.getMaxMileage()));
        }
        if (carFilter.getMinPower() != null) {
            criteria.add(Criteria.where("specs.enginePower").gte(carFilter.getMinPower()));
        }
        if (carFilter.getMaxPower() != null) {
            criteria.add(Criteria.where("specs.enginePower").lte(carFilter.getMaxPower()));
        }
        if (carFilter.getMinTorque() != null) {
            criteria.add(Criteria.where("specs.engineTorque").gte(carFilter.getMinTorque()));
        }
        if (carFilter.getMaxTorque() != null) {
            criteria.add(Criteria.where("specs.engineTorque").lte(carFilter.getMaxTorque()));
        }
        if (carFilter.getMinLength() != null) {
            criteria.add(Criteria.where("specs.length").gte(carFilter.getMinLength()));
        }
        if (carFilter.getMaxLength() != null) {
            criteria.add(Criteria.where("specs.length").lte(carFilter.getMaxLength()));
        }
        if (carFilter.getMinWidth() != null) {
            criteria.add(Criteria.where("specs.width").gte(carFilter.getMinWidth()));
        }
        if (carFilter.getMaxWidth() != null) {
            criteria.add(Criteria.where("specs.width").lte(carFilter.getMaxWidth()));
        }
        if (carFilter.getMinHeight() != null) {
            criteria.add(Criteria.where("specs.height").gte(carFilter.getMinHeight()));
        }
        if (carFilter.getMaxHeight() != null) {
            criteria.add(Criteria.where("specs.height").lte(carFilter.getMaxHeight()));
        }
        if (carFilter.getMinWheelbase() != null) {
            criteria.add(Criteria.where("specs.wheelbase").gte(carFilter.getMinWheelbase()));
        }
        if (carFilter.getMaxWheelbase() != null) {
            criteria.add(Criteria.where("specs.wheelbase").lte(carFilter.getMaxWheelbase()));
        }
        Criteria criterion = new Criteria().andOperator(criteria);
        Query query = Query.query(criterion);
        return mongoTemplate.find(query, CarModel.class);
    }
}
