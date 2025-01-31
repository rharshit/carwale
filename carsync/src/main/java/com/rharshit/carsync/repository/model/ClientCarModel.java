package com.rharshit.carsync.repository.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

@Data
@NoArgsConstructor
public abstract class ClientCarModel extends CarModel {
    @NonNull
    private String internalId;

    @Override
    public abstract String getClientId();

    public CarModel generateCarModel(CarModel clientCarModel) {
        return new CarModel(clientCarModel.getId(), getClientId(), clientCarModel.getMake(),
                clientCarModel.getModel(), clientCarModel.getVariant(), clientCarModel.getYear(),
                clientCarModel.getPrice(), clientCarModel.getMileage(), clientCarModel.getSpecs());
    }
}
