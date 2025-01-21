package com.rharshit.carsync.service;

import com.rharshit.carsync.repository.CarModelRepository;
import com.rharshit.carsync.repository.ConnectionRepository;
import com.rharshit.carsync.repository.model.CarModel;
import com.rharshit.carsync.repository.model.ConnectionItem;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CoreService {

    @Autowired
    private ConnectionRepository connectionRepository;

    @Autowired
    private CarModelRepository carModelRepository;

    @Value("${spring.application.name}")
    private String applicationName;

    @PostConstruct
    public void postInit() {
        log.info("Application name: {}", applicationName);
        test();
    }

    public String test() {
        if (testMongoConnection() == null) {
            return "Error in mongo connection";
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
            e.printStackTrace();
        }
        return null;
    }

    public List<CarModel> getCars() {
        return carModelRepository.findAll();
    }
}
