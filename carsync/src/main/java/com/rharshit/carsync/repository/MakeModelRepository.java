package com.rharshit.carsync.repository;


import com.rharshit.carsync.model.MakeModel;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.rharshit.carsync.common.Constants.CACHE_KEY_ALL_MODELS;

@Repository
public interface MakeModelRepository extends MongoRepository<MakeModel, String> {
    @NonNull
    @Cacheable(CACHE_KEY_ALL_MODELS)
    @Query(value = "{}", fields = "{ '_id': 1, 'models.variants.cars': 0}")
    List<MakeModel> findAllWithoutCars();

    @NonNull
    @Override
    @CacheEvict(value = CACHE_KEY_ALL_MODELS, allEntries = true)
    <S extends MakeModel> List<S> saveAll(@NonNull Iterable<S> entities);
}
