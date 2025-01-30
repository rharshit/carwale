package com.rharshit.carsync.repository.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

@Data
@NoArgsConstructor
public abstract class ClientCarModel extends CarModel {
    @NonNull
    private String internalId;

    public abstract String generateClientId();

    public CarModel generateCarModel(CarModel clientCarModel) {
        return new CarModel(clientCarModel.getId(), generateClientId(), clientCarModel.getMake(),
                clientCarModel.getModel(), clientCarModel.getVariant(), clientCarModel.getYear(),
                clientCarModel.getPrice(), clientCarModel.getMileage(), clientCarModel.getSpecs());
    }
}
