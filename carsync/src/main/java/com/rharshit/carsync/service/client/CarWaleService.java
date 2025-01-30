package com.rharshit.carsync.service.client;

import com.rharshit.carsync.repository.model.client.CarWaleCarModel;
import com.rharshit.carsync.service.ClientService;
import org.springframework.stereotype.Service;

@Service
public class CarWaleService extends ClientService<CarWaleCarModel> {

    // TODO: Implement this method
    @Override
    public String fetchAllCars() {
        return "Started fetching cars from " + getClientName();
    }

    @Override
    public String getClientId() {
        return "carwale";
    }

    @Override
    public String getClientName() {
        return "CarWale";
    }
}
