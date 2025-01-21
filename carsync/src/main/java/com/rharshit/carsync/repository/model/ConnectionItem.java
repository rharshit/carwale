package com.rharshit.carsync.repository.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.Nullable;

@Document("connection")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ConnectionItem {
    @Id
    @Nullable
    private String id;
    @Nullable
    private String client;
    @Nullable
    private Long lastConnection;
}
