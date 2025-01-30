package com.rharshit.carsync.service;

import com.rharshit.carsync.repository.model.ClientCarModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Slf4j
@Service
public abstract class ClientService<T extends ClientCarModel> {

    private Thread fetchThread;

    private RestClient restClient;

    public abstract String getClientId();

    public abstract String getClientName();

    public abstract String getClientDomain();

    public abstract void fetchAllCars();

    protected synchronized RestClient getRestClient() {
        if (restClient == null) {
            restClient = RestClient.builder().baseUrl(getClientDomain()).build();
        }
        return restClient;
    }

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
                    long startTime = System.currentTimeMillis();
                    fetchAllCars();
                    log.info("Fetched cars from " + getClientName() + " in " + (System.currentTimeMillis() - startTime) + "ms");
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

    protected float getPercentage(int total, int fetched) {
        return (float) (fetched * 100) / total;
    }
}
