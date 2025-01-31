package com.rharshit.carsync.service;

import com.rharshit.carsync.repository.CarModelRepository;
import com.rharshit.carsync.repository.MakeModelRepository;
import com.rharshit.carsync.repository.model.CarModel;
import com.rharshit.carsync.repository.model.ClientCarModel;
import com.rharshit.carsync.repository.model.MakeModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public abstract class ClientService<T extends ClientCarModel> {

    @Autowired
    private CarModelRepository carModelRepository;

    @Autowired
    private MakeModelRepository makeModelRepository;

    protected final List<CarModel> carPushList = Collections.synchronizedList(new ArrayList<>());

    private Thread fetchThread;

    private RestClient restClient;

    public abstract String getClientId();

    public abstract String getClientName();

    public abstract String getClientDomain();

    public abstract void fetchAllCars();

    protected final List<CarModel> carStagingList = Collections.synchronizedList(new ArrayList<>());

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
     * @param clientCarModel
     */
    protected void pushCar(T clientCarModel) {
        CarModel carModel = clientCarModel.generateCarModel();
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
                if (pushed) {
                    updateMakeModel(saved);
                } else {
                    throw new RuntimeException("Error saving cars to DB");
                }
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

    private void updateMakeModel(List<CarModel> savedCarModels) {
        if (savedCarModels.isEmpty()) {
            return;
        }
        List<MakeModel> makesToSave = getMakesToSave(savedCarModels);
        List<MakeModel> savedMakes = makeModelRepository.saveAll(makesToSave);
        log.info("updateMakeModel : Saved {} makes to DB", savedMakes.size());
    }

    private List<MakeModel> getMakesToSave(List<CarModel> carModels) {
        List<MakeModel> makes = makeModelRepository.findAllById(carModels.stream().map(CarModel::getMake).distinct().toList());
        for (CarModel carModel : carModels) {
            MakeModel currentMake = makes.stream().filter(make -> make.getMake().equals(carModel.getMake())).findFirst().orElse(null);
            if (currentMake == null) {
                currentMake = new MakeModel(carModel.getMake());
                makes.add(currentMake);
            }
            MakeModel.Model currentModel = currentMake.getModels().stream().filter(model -> model.getName().equals(carModel.getModel())).findFirst().orElse(null);
            if (currentModel == null) {
                currentModel = new MakeModel.Model(carModel.getModel());
                currentMake.getModels().add(currentModel);
            }

            MakeModel.Variant currentVariant = currentModel.getVariants().stream().filter(variant -> variant.getName().equals(carModel.getVariant())).findFirst().orElse(null);
            if (currentVariant == null) {
                currentVariant = new MakeModel.Variant(carModel.getVariant());
                currentModel.getVariants().add(currentVariant);
            }

            currentVariant.getCars().add(carModel.getId());
        }
        return makes;
    }

    // TODO: Optimize this method
    protected boolean isCarDetailFetched(String clientId) {
        CarModel carModel = new CarModel();
        carModel.setClientId(clientId);
        return carModelRepository.exists(Example.of(carModel));
    }
}
