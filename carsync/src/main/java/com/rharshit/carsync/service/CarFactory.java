package com.rharshit.carsync.service;

import com.rharshit.carsync.model.ClientCarModel;
import com.rharshit.carsync.service.client.CarWaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.rharshit.carsync.common.Constants.CLIENT_ID_CARWALE;

@Component
public class CarFactory {

    @Autowired
    private CarWaleService carWaleService;

    protected ClientService<? extends ClientCarModel> getClientService(String client) {
        switch (client) {
            case CLIENT_ID_CARWALE:
                return carWaleService;
            default:
                return null;
        }
    }
}
