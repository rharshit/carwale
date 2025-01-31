package com.rharshit.carsync.repository;

import com.rharshit.carsync.repository.model.CarModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarModelRepository extends MongoRepository<CarModel, String> {
}
