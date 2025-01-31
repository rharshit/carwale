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
    @NonNull
    public abstract String getClient();

    @Override
    @NonNull
    public String getClientId() {
        return getClient() + "-" + getInternalId();
    }

    public CarModel generateCarModel() {
        return new CarModel(getId(), getClient(), getClientId(), getMake(), getModel(), getVariant(), getYear(),
                getPrice(), getMileage(), getUrl(), getSpecs());
    }
}
