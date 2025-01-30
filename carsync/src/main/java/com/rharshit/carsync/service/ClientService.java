package com.rharshit.carsync.service;

import com.rharshit.carsync.repository.model.ClientCarModel;
import org.springframework.stereotype.Service;

@Service
public abstract class ClientService<T extends ClientCarModel> {

    /**
     * Fetch all cars from the client
     *
     * @return
     */
    public abstract String fetchAllCars();

    public abstract String getClientId();

    public abstract String getClientName();
}
