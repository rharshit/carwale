package com.rharshit.carsync.repository;


import com.rharshit.carsync.repository.model.MakeModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MakeModelRepository extends MongoRepository<MakeModel, String> {
}
