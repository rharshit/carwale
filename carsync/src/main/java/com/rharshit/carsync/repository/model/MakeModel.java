package com.rharshit.carsync.repository.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
@Document("makeModel")
@NoArgsConstructor
public class MakeModel {
    @Id
    private String make;
    private Set<Model> models;

    public MakeModel(String make) {
        this(make, new HashSet<>());
    }

    public MakeModel(String make, Set<Model> models) {
        this.make = make;
        this.models = models;
    }

    @Data
    @NoArgsConstructor
    public static class Model {
        private String name;
        private Set<Variant> variants;

        public Model(String name) {
            this(name, new HashSet<>());
        }

        public Model(String name, Set<Variant> variants) {
            this.name = name;
            this.variants = variants;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Model model = (Model) o;
            return Objects.equals(name, model.name);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(name);
        }
    }

    @Data
    @NoArgsConstructor
    public static class Variant {
        private String name;
        private Set<String> cars;

        public Variant(String name) {
            this(name, new HashSet<>());
        }

        public Variant(String name, Set<String> cars) {
            this.name = name;
            this.cars = cars;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Variant variant = (Variant) o;
            return Objects.equals(name, variant.name);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(name);
        }
    }
}
