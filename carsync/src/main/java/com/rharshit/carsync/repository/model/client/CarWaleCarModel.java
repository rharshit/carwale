package com.rharshit.carsync.repository.model.client;

import com.rharshit.carsync.repository.model.ClientCarModel;
import lombok.Data;

@Data
public class CarWaleCarModel extends ClientCarModel {

    @Override
    public String generateClientId() {
        return "carwale_" + getInternalId();
    }
}
