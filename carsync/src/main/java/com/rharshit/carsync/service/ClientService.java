package com.rharshit.carsync.service;

import com.rharshit.carsync.repository.model.ClientCarModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public abstract class ClientService<T extends ClientCarModel> {

    private Thread fetchThread;

    public abstract String getClientId();

    public abstract String getClientName();

    public abstract void fetchAllCars();

    /**
     * Fetch all cars from the client
     *
     * @return
     */
    public synchronized String startFetchThread() {
        log.info("Starting to fetch cars from " + getClientName());
        if (fetchThread == null || !fetchThread.isAlive()) {
            fetchThread = new Thread(() -> {
                try {
                    log.info("Fetching cars from " + getClientName());
                    fetchAllCars();
                    log.info("Fetched cars from " + getClientName());
                } catch (Exception e) {
                    log.error("Error fetching cars for " + getClientName(), e);
                }
            });
            fetchThread.setName("fetchThread-" + getClientId());
            fetchThread.start();
            return "Started fetching cars from " + getClientName();
        } else {
            return "Already fetching cars from " + getClientName();
        }
    }
}
