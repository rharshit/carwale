package com.rharshit.carsync.repository;

import com.rharshit.carsync.repository.model.ConnectionItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConnectionRepository extends MongoRepository<ConnectionItem, String> {

}
