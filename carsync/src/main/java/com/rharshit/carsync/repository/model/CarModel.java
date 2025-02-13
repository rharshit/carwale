package com.rharshit.carsync.repository.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.Objects;

@Document("carModel")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarModel {
    @Id
    private String id;
    @NonNull
    private String client;
    @NonNull
    private String clientId;
    @NonNull
    private String make;
    @NonNull
    private String model;
    @NonNull
    private String variant;
    @NonNull
    private Integer year;
    @NonNull
    private Integer price;
    @NonNull
    private Integer mileage;
    @NonNull
    private String url;
    @NonNull
    private Specs specs = new Specs();

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CarModel carModel = (CarModel) o;
        return Objects.equals(id, carModel.id) && Objects.equals(client, carModel.client) && Objects.equals(clientId, carModel.clientId) && Objects.equals(make, carModel.make) && Objects.equals(model, carModel.model) && Objects.equals(variant, carModel.variant) && Objects.equals(year, carModel.year) && Objects.equals(url, carModel.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, client, clientId, make, model, variant, year, url);
    }

    @Data
    @NoArgsConstructor
    public static class Specs {
        @Nullable
        private String fuelType;
        @Nullable
        private String transmissionType;
        @Nullable
        private String engineType;
        @Nullable
        private Integer engineDisplacement;
        @Nullable
        private Integer enginePower;
        @Nullable
        private Integer engineTorque;
        @Nullable
        private Integer length;
        @Nullable
        private Integer width;
        @Nullable
        private Integer height;
        @Nullable
        private Integer wheelbase;
        @Nullable
        private Integer groundClearance;
        @Nullable
        private Integer kerbWeight;
        @Nullable
        private Integer bootSpace;
        @Nullable
        private String drivetrain;
    }
}
