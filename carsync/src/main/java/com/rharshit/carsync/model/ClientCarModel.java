package com.rharshit.carsync.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

@EqualsAndHashCode(callSuper = true)
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
        return new CarModel(getId(), getClient(), getClientId(), getCity(), getMake(), getModel(), getVariant(), getYear(),
                getPrice(), getMileage(), getUrl(), getImageUrls(), getSpecs());
    }
}
