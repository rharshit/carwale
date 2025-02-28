package com.rharshit.carsync.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Objects;

@Document("carModels")
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
    private String city;
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
    private List<String> imageUrls;
    @NonNull
    private Specs specs = new Specs();
    @NonNull
    private Long createdAt;
    @NonNull
    private Long updatedAt;
    @NonNull
    private Long validatedAt;

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

    /*
    Update created at field only if it does not already exist
     */
    public void setCreatedAt(@NonNull Long createdAt) {
        if (this.createdAt == null || this.createdAt == 0) {
            this.createdAt = createdAt;
        }
    }

    @NonNull
    public Long getCreatedAt() {
        return createdAt == null ? 0 : createdAt;
    }

    @NonNull
    public Long getUpdatedAt() {
        return updatedAt == null ? 0 : updatedAt;
    }

    @NonNull
    public Long getValidatedAt() {
        return validatedAt == null ? 0 : validatedAt;
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

    public CarModel generateCarModel() {
        return new CarModel(getId(), getClient(), getClientId(), getCity(), getMake(), getModel(), getVariant(), getYear(),
                getPrice(), getMileage(), getUrl(), getImageUrls(), getSpecs(),
                getCreatedAt() == null ? getCreatedAt() : 0,
                getUpdatedAt() == null ? getUpdatedAt() : System.currentTimeMillis(),
                getValidatedAt() == null ? getValidatedAt() : System.currentTimeMillis());
    }
}
