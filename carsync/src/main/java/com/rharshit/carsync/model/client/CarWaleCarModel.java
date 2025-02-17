package com.rharshit.carsync.model.client;

import com.rharshit.carsync.model.ClientCarModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.lang.NonNull;

import static com.rharshit.carsync.common.Constants.CLIENT_ID_CARWALE;

@EqualsAndHashCode(callSuper = true)
@Data
public class CarWaleCarModel extends ClientCarModel {

    public CarWaleCarModel(String internalId) {
        super();
        this.setInternalId(internalId);
    }

    @Override
    @NonNull
    public String getClient() {
        return CLIENT_ID_CARWALE;
    }
}
