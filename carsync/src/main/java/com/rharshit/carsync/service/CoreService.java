package com.rharshit.carsync.service;

import com.rharshit.carsync.repository.ConnectionRepository;
import com.rharshit.carsync.repository.model.ConnectionItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class CoreService {

    @Autowired
    private ConnectionRepository connectionRepository;

    @Value("${spring.application.name}")
    private String applicationName;

    public String test() {
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
            return saved.getId();
        } catch (Exception e) {
            log.error("Error saving item", e);
            e.printStackTrace();
        }
        return null;
    }
}
