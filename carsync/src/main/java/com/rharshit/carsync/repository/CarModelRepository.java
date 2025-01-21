package com.rharshit.carsync.repository;

import com.rharshit.carsync.repository.model.CarModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CarModelRepository extends MongoRepository<CarModel, String> {
}
