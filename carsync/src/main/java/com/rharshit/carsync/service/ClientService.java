package com.rharshit.carsync.service;

import com.rharshit.carsync.repository.CarModelRepository;
import com.rharshit.carsync.repository.model.CarModel;
import com.rharshit.carsync.repository.model.ClientCarModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public abstract class ClientService<T extends ClientCarModel> {

    protected final List<CarModel> carPushList = Collections.synchronizedList(new ArrayList<>());

    private Thread fetchThread;

    private RestClient restClient;

    public abstract String getClientId();

    public abstract String getClientName();

    public abstract String getClientDomain();

    public abstract void fetchAllCars();
    protected final List<CarModel> carStagingList = Collections.synchronizedList(new ArrayList<>());
    @Autowired
    private CarModelRepository carModelRepository;

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

    /**
     * Push car to staging list
     *
     * @param carModel
     */
    protected void pushCar(CarModel carModel) {
        synchronized (carPushList) {
            carPushList.add(carModel);
        }
    }

    /**
     * Push list of cars to staging list
     *
     * @param carModels
     */
    protected void pushCar(List<CarModel> carModels) {
        synchronized (carPushList) {
            carPushList.addAll(carModels);
        }
    }

    @Scheduled(fixedRate = 1000)
    public void pushCarsToStaging() {
        synchronized (carPushList) {
            synchronized (carStagingList) {
                carStagingList.addAll(carPushList);
                carPushList.clear();
            }
        }
    }

    @Scheduled(fixedDelay = 1)
    public void pushCarsToDB() {
        List<CarModel> carsToPush = new ArrayList<>();
        synchronized (carStagingList) {
            carsToPush.addAll(carStagingList);
            carStagingList.clear();
        }
        if (carsToPush.isEmpty()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } else {
            boolean pushed = false;
            try {
                List<CarModel> saved = carModelRepository.saveAll(carsToPush);
                log.debug("pushCarsToDB : Saved {} cars to DB", saved.size());
                pushed = saved.size() == carsToPush.size();
            } catch (Exception e) {
                log.error("Error saving car", e);
            } finally {
                if (!pushed) {
                    log.warn("Pushing cars to DB failed. Pushing back to staging list.");
                    synchronized (carStagingList) {
                        carStagingList.addAll(carsToPush);
                    }
                }
            }
        }
    }
}
