package com.rharshit.carsync.service;

import com.rharshit.carsync.repository.ConnectionRepository;
import com.rharshit.carsync.repository.model.ConnectionItem;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class CoreService {

    @Autowired
    private ApplicationContext appContext;

    @Autowired
    private ConnectionRepository connectionRepository;

    @Value("${spring.application.name}")
    private String applicationName;

    @PostConstruct
    public void postInit() {
        log.info("Application name: {}", applicationName);
        try {
            test();
        } catch (Exception e) {
            log.error("Error initializing Core service, shutting down.", e);
            SpringApplication.exit(appContext, () -> 1);
        }
    }

    public String test() {
        if (testMongoConnection() == null) {
            throw new RuntimeException("MongoDB connection test failed");
        }
        return "Test successful!";
    }

    public String testMongoConnection() {
        try {
            Optional<ConnectionItem> optionalItem = connectionRepository.findOne(Example.of(new ConnectionItem(null, applicationName, null)));
            ConnectionItem item;
            if (optionalItem.isPresent()) {
                item = optionalItem.get();
                log.trace("Item found with id: {}", item.getId());
                log.trace("Item found with client: {}", item.getClient());
                log.trace("Item found with lastConnection: {}", item.getLastConnection());
            } else {
                log.trace("Item not found, creating new item");
                item = new ConnectionItem();
                item.setClient(applicationName);
            }
            item.setLastConnection(System.currentTimeMillis());
            ConnectionItem saved = connectionRepository.save(item);
            log.trace("Item saved with id: {}", saved.getId());
            log.trace("Item saved with client: {}", saved.getClient());
            log.trace("Item saved with lastConnection: {}", saved.getLastConnection());
            log.info("MongoDB connection test successful");
            return saved.getId();
        } catch (Exception e) {
            log.error("Error saving item", e);
        }
        return null;
    }

}
