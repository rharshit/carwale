package com.rharshit.carsync.service.client;

import com.rharshit.carsync.repository.model.client.CarWaleCarModel;
import com.rharshit.carsync.service.ClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CarWaleService extends ClientService<CarWaleCarModel> {

    // TODO: Implement this method
    @Override
    public void fetchAllCars() {
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
