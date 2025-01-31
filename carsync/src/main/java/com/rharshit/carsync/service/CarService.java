package com.rharshit.carsync.service;

import com.rharshit.carsync.repository.CarModelRepository;
import com.rharshit.carsync.repository.model.CarModel;
import com.rharshit.carsync.repository.model.ClientCarModel;
import com.rharshit.carsync.service.client.CarWaleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static com.rharshit.carsync.common.Constants.CLIENT_ID_CARWALE;

@Slf4j
@Service
public class CarService {

    @Autowired
    private CarModelRepository carModelRepository;

    @Autowired
    private CarWaleService carWaleService;

    public List<CarModel> getCars() {
        try {
            return carModelRepository.findAll();
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
        ClientService<? extends ClientCarModel> clientService = getClientService(client);
        if (clientService == null) {
            return "Client not found";
        }
        return clientService.startFetchThread();
    }

    private ClientService<? extends ClientCarModel> getClientService(String client) {
        switch (client) {
            case CLIENT_ID_CARWALE:
                return carWaleService;
            default:
                return null;
        }
    }
}
