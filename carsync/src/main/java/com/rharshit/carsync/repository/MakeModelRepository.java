package com.rharshit.carsync.repository;


import com.rharshit.carsync.model.MakeModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MakeModelRepository extends MongoRepository<MakeModel, String> {
    @NonNull
    @Override
    @Query(value = "{}", fields = "{ '_id': 1, 'models.variants.cars': 0}")
    List<MakeModel> findAll();
}
