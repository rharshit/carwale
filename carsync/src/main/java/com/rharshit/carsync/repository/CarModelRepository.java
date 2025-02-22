package com.rharshit.carsync.repository;

import com.rharshit.carsync.model.CarModel;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.rharshit.carsync.common.Constants.CACHE_KEY_ALL_CARS;

@Repository
public interface CarModelRepository extends MongoRepository<CarModel, String>, AppRepository {

    @NonNull
    @Override
    @Cacheable(CACHE_KEY_ALL_CARS)
    List<CarModel> findAll();

    @NonNull
    @Override
    @CacheEvict(value = CACHE_KEY_ALL_CARS, allEntries = true)
    <S extends CarModel> S insert(@NonNull S entity);

    @NonNull
    @Override
    @CacheEvict(value = CACHE_KEY_ALL_CARS, allEntries = true)
    <S extends CarModel> List<S> insert(@NonNull Iterable<S> entities);

    @NonNull
    @Override
    @CacheEvict(value = CACHE_KEY_ALL_CARS, allEntries = true)
    <S extends CarModel> List<S> saveAll(@NonNull Iterable<S> entities);

    @NonNull
    @Override
    @CacheEvict(value = CACHE_KEY_ALL_CARS, allEntries = true)
    <S extends CarModel> S save(@NonNull S entity);

    @Override
    @CacheEvict(value = CACHE_KEY_ALL_CARS, allEntries = true)
    void deleteById(@NonNull String s);

    @Override
    @CacheEvict(value = CACHE_KEY_ALL_CARS, allEntries = true)
    void delete(@NonNull CarModel entity);

    @Override
    @CacheEvict(value = CACHE_KEY_ALL_CARS, allEntries = true)
    void deleteAllById(@NonNull Iterable<? extends String> strings);

    @Override
    @CacheEvict(value = CACHE_KEY_ALL_CARS, allEntries = true)
    void deleteAll(@NonNull Iterable<? extends CarModel> entities);

    @Override
    @CacheEvict(value = CACHE_KEY_ALL_CARS, allEntries = true)
    void deleteAll();

    @Query("{'city' : { $exists: false }}")
    List<CarModel> findCarsWithoutCity();
}
