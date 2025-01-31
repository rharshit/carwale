package com.rharshit.carsync.repository;

import com.rharshit.carsync.repository.model.CarModel;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarModelRepository extends MongoRepository<CarModel, String> {

    @Override
    @CacheEvict(value = "allCars", allEntries = true)
    <S extends CarModel> S insert(S entity);

    @Override
    @CacheEvict(value = "allCars", allEntries = true)
    <S extends CarModel> List<S> insert(Iterable<S> entities);

    @Override
    @CacheEvict(value = "allCars", allEntries = true)
    <S extends CarModel> List<S> saveAll(Iterable<S> entities);

    @Override
    @CacheEvict(value = "allCars", allEntries = true)
    <S extends CarModel> S save(S entity);

    @Override
    @CacheEvict(value = "allCars", allEntries = true)
    void deleteById(String s);

    @Override
    @CacheEvict(value = "allCars", allEntries = true)
    void delete(CarModel entity);

    @Override
    @CacheEvict(value = "allCars", allEntries = true)
    void deleteAllById(Iterable<? extends String> strings);

    @Override
    @CacheEvict(value = "allCars", allEntries = true)
    void deleteAll(Iterable<? extends CarModel> entities);

    @Override
    @CacheEvict(value = "allCars", allEntries = true)
    void deleteAll();
}
