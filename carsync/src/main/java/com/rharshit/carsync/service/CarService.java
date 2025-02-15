package com.rharshit.carsync.service;

import com.rharshit.carsync.repository.CarModelRepository;
import com.rharshit.carsync.repository.model.CarFilter;
import com.rharshit.carsync.repository.model.CarModel;
import com.rharshit.carsync.repository.model.ClientCarModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;


@Slf4j
@Service
public class CarService {

    @Autowired
    private CarModelRepository carModelRepository;

    @Autowired
    private CarFactory carFactory;

    @CachePut("allCars")
    public List<CarModel> getCars(CarFilter carFilter) {
        try {
            verifyFilter(carFilter);
            return carModelRepository.findByFilter(
                    carFilter.getCity(), carFilter.getMake(), carFilter.getModel(), carFilter.getVariant(),
                    carFilter.getMinYear(), carFilter.getMaxYear(),
                    carFilter.getMinPrice(), carFilter.getMaxPrice(),
                    carFilter.getMinMileage(), carFilter.getMaxMileage(),
                    carFilter.getMinPower(), carFilter.getMaxPower(),
                    carFilter.getMinTorque(), carFilter.getMaxTorque(),
                    carFilter.getMinLength(), carFilter.getMaxLength(),
                    carFilter.getMinWidth(), carFilter.getMaxWidth(),
                    carFilter.getMinHeight(), carFilter.getMaxHeight(),
                    carFilter.getMinWheelbase(), carFilter.getMaxWheelbase());
        } catch (Exception e) {
            log.error("Error fetching list of cars", e);
            return Collections.emptyList();
        }
    }

    private void verifyFilter(CarFilter carFilter) {
        if (carFilter == null) {
            throw new IllegalArgumentException("Filter cannot be null");
        }
        if (carFilter.getCity() == null) {
            carFilter.setCity("/*");
        }
        if (carFilter.getMake() == null) {
            carFilter.setMake("/*");
        }
        if (carFilter.getModel() == null) {
            carFilter.setModel("/*");
        }
        if (carFilter.getVariant() == null) {
            carFilter.setVariant("/*");
        }
        if (carFilter.getMinYear() == null) {
            carFilter.setMinYear(0);
        }
        if (carFilter.getMaxYear() == null) {
            carFilter.setMaxYear(Integer.MAX_VALUE);
        }
        if (carFilter.getMinPrice() == null) {
            carFilter.setMinPrice(0);
        }
        if (carFilter.getMaxPrice() == null) {
            carFilter.setMaxPrice(Integer.MAX_VALUE);
        }
        if (carFilter.getMinMileage() == null) {
            carFilter.setMinMileage(0);
        }
        if (carFilter.getMaxMileage() == null) {
            carFilter.setMaxMileage(Integer.MAX_VALUE);
        }
        if (carFilter.getMinPower() == null) {
            carFilter.setMinPower(0);
        }
        if (carFilter.getMaxPower() == null) {
            carFilter.setMaxPower(Integer.MAX_VALUE);
        }
        if (carFilter.getMinTorque() == null) {
            carFilter.setMinTorque(0);
        }
        if (carFilter.getMaxTorque() == null) {
            carFilter.setMaxTorque(Integer.MAX_VALUE);
        }
        if (carFilter.getMinLength() == null) {
            carFilter.setMinLength(0);
        }
        if (carFilter.getMaxLength() == null) {
            carFilter.setMaxLength(Integer.MAX_VALUE);
        }
        if (carFilter.getMinWidth() == null) {
            carFilter.setMinWidth(0);
        }
        if (carFilter.getMaxWidth() == null) {
            carFilter.setMaxWidth(Integer.MAX_VALUE);
        }
        if (carFilter.getMinHeight() == null) {
            carFilter.setMinHeight(0);
        }
        if (carFilter.getMaxHeight() == null) {
            carFilter.setMaxHeight(Integer.MAX_VALUE);
        }
        if (carFilter.getMinWheelbase() == null) {
            carFilter.setMinWheelbase(0);
        }
        if (carFilter.getMaxWheelbase() == null) {
            carFilter.setMaxWheelbase(Integer.MAX_VALUE);
        }
    }

    public boolean addCar(List<CarModel> carModels) {
        try {
            List<CarModel> saved = carModelRepository.saveAll(carModels);
            return saved.size() == carModels.size();
        } catch (Exception e) {
            log.error("Error saving car", e);
            return false;
        }
    }

    public String startFetching(String client) {
        ClientService<? extends ClientCarModel> clientService = carFactory.getClientService(client);
        if (clientService == null) {
            return "Client not found";
        }
        return clientService.startFetchThread();
    }

    public String startFixing(String client) {
        ClientService<? extends ClientCarModel> clientService = carFactory.getClientService(client);
        if (clientService == null) {
            return "Client not found";
        }
        return clientService.startFixThread();
    }

}
