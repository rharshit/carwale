package com.rharshit.carsync.model;

import lombok.Data;

@Data
public abstract class ResponseModel {
    private boolean success = true;
    private String error = null;
}
