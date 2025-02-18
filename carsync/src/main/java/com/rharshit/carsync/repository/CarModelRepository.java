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

    @Cacheable(CACHE_KEY_ALL_CARS)
    @Query("{'city' : {$regex : ?0}, 'make' : {$regex : ?1}, 'model' : {$regex : ?2}, 'variant' : {$regex : ?3}," +
            "'year' : {$gte : ?4, $lte : ?5}," +
            "'price' : {$gte : ?6, $lte : ?7}," +
            "'mileage' : {$gte : ?8, $lte : ?9}," +
            "'specs.enginePower' : {$gte : ?10, $lte : ?11}," +
            "'specs.engineTorque' : {$gte : ?12, $lte : ?13}," +
            "'specs.length' : {$gte : ?14, $lte : ?15}," +
            "'specs.width' : {$gte : ?16, $lte : ?17}," +
            "'specs.height' : {$gte : ?18, $lte : ?19}," +
            "'specs.wheelbase' : {$gte : ?20, $lte : ?21}}")
    List<CarModel> findByFilter(String cities, String makes, String models, String variants,
                                Integer minYear, Integer maxYear,
                                Integer minPrice, Integer maxPrice,
                                Integer minMileage, Integer maxMileage,
                                Integer minPower, Integer maxPower,
                                Integer minTorque, Integer maxTorque,
                                Integer minLength, Integer maxLength,
                                Integer minWidth, Integer maxWidth,
                                Integer minHeight, Integer maxHeight,
                                Integer minWheelbase, Integer maxWheelbase);

    @Query("{'city' : { $exists: false }}")
    List<CarModel> findCarsWithoutCity();
}
