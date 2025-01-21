package com.rharshit.carsync.repository.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.Nullable;

@Document("carModel")
@Data
@AllArgsConstructor
public class CarModel {
    @Id
    private String id;
    private String make;
    private String model;
    private Integer year;
    private Integer price;
    private Integer mileage;
    private Specs specs;

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
        private String engineDisplacement;
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
