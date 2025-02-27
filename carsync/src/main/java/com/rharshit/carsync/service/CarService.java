package com.rharshit.carsync.service;

import com.rharshit.carsync.model.*;
import com.rharshit.carsync.repository.CarModelRepository;
import com.rharshit.carsync.repository.MakeModelRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class CarService {

    @Autowired
    private CarModelRepository carModelRepository;

    @Autowired
    private MakeModelRepository makeModelRepository;

    @Autowired
    private CarFactory carFactory;

    public AllCarsResponse getCars(CarFilter carFilter) {
        AllCarsResponse response = new AllCarsResponse();
        try {
            List<CarModel> cars = getCarsByFilter(carFilter);
            int skip = carFilter.getSkip() == null ? 0 : carFilter.getSkip();
            int limit = carFilter.getLimit() == null ? Integer.MAX_VALUE : carFilter.getLimit();
            response.setCars(cars.stream().skip(skip).limit(limit).toList());
            response.setTotal(cars.size());
            response.setLength(response.getCars().size());
            response.setLoadMore(skip + response.getCars().size() < cars.size());
        } catch (Exception e) {
            response.setError(e.getLocalizedMessage());
            response.setSuccess(false);
        }
        return response;
    }

    public List<CarModel> getCarsByFilter(CarFilter carFilter) {
        try {
            return carModelRepository.findByFilters(carFilter);
        } catch (Exception e) {
            log.error("Error fetching list of cars", e);
            return Collections.emptyList();
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

    public CarFilterResponse getCarFilterValues() {
        try {
            CarFilterResponse carFilterResponse = new CarFilterResponse();
            carFilterResponse.setValues(carModelRepository.getCarFilterValues());
            carFilterResponse.setMakeModels(makeModelRepository.findAllWithoutCars());
            carFilterResponse.setCities(carModelRepository.getAllCities());
            return carFilterResponse;
        } catch (Exception e) {
            log.error("Error getting car filters", e);
            CarFilterResponse carFilterResponse = new CarFilterResponse();
            carFilterResponse.setSuccess(false);
            return carFilterResponse;
        }
    }

    public String startCleanup(String client) {
        ClientService<? extends ClientCarModel> clientService = carFactory.getClientService(client);
        if (clientService == null) {
            return "Client not found";
        }
        return clientService.startCleanupThread();
    }
}
