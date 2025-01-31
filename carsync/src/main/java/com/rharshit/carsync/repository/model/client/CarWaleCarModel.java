package com.rharshit.carsync.repository.model.client;

import com.rharshit.carsync.repository.model.ClientCarModel;
import lombok.Data;
import org.springframework.lang.NonNull;

@Data
public class CarWaleCarModel extends ClientCarModel {

    public CarWaleCarModel(String internalId) {
        super();
        this.setInternalId(internalId);
    }

    @Override
    @NonNull
    public String getClient() {
        return "carwale";
    }
}
