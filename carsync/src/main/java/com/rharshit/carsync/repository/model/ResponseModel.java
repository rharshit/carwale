package com.rharshit.carsync.repository.model;

import lombok.Data;

@Data
public abstract class ResponseModel {
    private boolean success = false;
    private String error = null;
}
