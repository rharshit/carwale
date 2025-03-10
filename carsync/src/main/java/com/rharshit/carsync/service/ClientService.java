package com.rharshit.carsync.service;

import com.rharshit.carsync.model.CarModel;
import com.rharshit.carsync.model.ClientCarModel;
import com.rharshit.carsync.model.MakeModel;
import com.rharshit.carsync.repository.CarModelRepository;
import com.rharshit.carsync.repository.MakeModelRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

@Slf4j
@Service
public abstract class ClientService<T extends ClientCarModel> {

    @Autowired
    protected CarModelRepository carModelRepository;

    @Autowired
    private MakeModelRepository makeModelRepository;

    protected final List<CarModel> carPushList = Collections.synchronizedList(new ArrayList<>());

    protected final List<CarModel> carDeleteList = Collections.synchronizedList(new ArrayList<>());

    private Thread fetchThread;

    private Thread fixThread;

    private Thread cleanupThread;

    protected int cleanupCount;

    public abstract String getClientId();

    public abstract String getClientName();

    public abstract String getClientDomain();

    public abstract void fetchAllCars();

    protected abstract void fixAllCars();

    protected abstract void cleanupData();

    protected final List<CarModel> carStagingList = Collections.synchronizedList(new ArrayList<>());
    protected final List<CarModel> carPruningList = Collections.synchronizedList(new ArrayList<>());

    private static final List<CarModel> carCheckList = Collections.synchronizedList(new ArrayList<>());
    private static final List<CarModel> carExistList = Collections.synchronizedList(new ArrayList<>());
    private static final List<CarModel> carNotExistList = Collections.synchronizedList(new ArrayList<>());

    /**
     * Fetch all cars from the client
     *
     * @return
     */
    public synchronized String startFetchThread() {
        log.info("Starting to fetch cars from {}", getClientName());
        if (fetchThread == null || !fetchThread.isAlive()) {
            fetchThread = new Thread(() -> {
                try {
                    log.info("Fetching cars from {}", getClientName());
                    long startTime = System.currentTimeMillis();
                    fetchAllCars();
                    log.info("Fetched cars from {} in {}ms", getClientName(), System.currentTimeMillis() - startTime);
                } catch (Exception e) {
                    log.error("Error fetching cars for {}", getClientName(), e);
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
    protected <C extends CarModel> void pushCar(C carModel) {
        synchronized (carPushList) {
            carPushList.add(carModel);
        }
    }

    /**
     * Push list of cars to staging list
     *
     * @param carModels
     */
    protected <C extends CarModel> void pushCars(List<C> carModels) {
        synchronized (carPushList) {
            carPushList.addAll(carModels);
        }
    }

    @SneakyThrows
    @Scheduled(fixedDelay = 1)
    public void pushCarsToStaging() {
        if(carPushList.isEmpty()) {
            Thread.sleep(500);
        } else {
            synchronized (carPushList) {
                synchronized (carStagingList) {
                    carStagingList.addAll(carPushList);
                    carPushList.clear();
                }
            }
        }
    }

    @Scheduled(fixedDelay = 1)
    public void pushCarsToDB() {
        List<CarModel> carsToPush;
        synchronized (carStagingList) {
            carsToPush = new ArrayList<>(carStagingList);
            carStagingList.clear();
        }
        List<CarModel> carsToPushConverted = carsToPush.stream().map(CarModel::generateCarModel).toList();
        if (carsToPushConverted.isEmpty()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } else {
            long startTime = System.currentTimeMillis();
            log.info("pushCarsToDB : Pushing {} cars to DB", carsToPushConverted.size());
            boolean pushed = false;
            try {
                List<CarModel> saved = carModelRepository.saveAll(carsToPushConverted);
                log.debug("pushCarsToDB : Saved {} cars to DB", saved.size());
                pushed = saved.size() == carsToPushConverted.size();
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
            log.info("pushCarsToDB : Pushed {} cars to DB in {}ms", carsToPushConverted.size(), System.currentTimeMillis() - startTime);
        }
    }

    protected <C extends CarModel> void deleteCar(C clientCarModel) {
        synchronized (carDeleteList) {
            carDeleteList.add(clientCarModel);
        }
    }

    @SneakyThrows
    @Scheduled(fixedDelay = 1)
    public void carsToPrune() {
        if(carDeleteList.isEmpty()) {
            Thread.sleep(500);
        } else {
            synchronized (carDeleteList) {
                synchronized (carPruningList) {
                    carPruningList.addAll(carDeleteList);
                    carDeleteList.clear();
                }
            }
        }
    }

    @SneakyThrows
    @Scheduled(fixedDelay = 1)
    public void pruneCars() {
        List<CarModel> carsToPrune;
        synchronized (carPruningList) {
            carsToPrune = new ArrayList<>(carPruningList);
            carPruningList.clear();
        }
        if(carsToPrune.isEmpty()) {
            Thread.sleep(100);
        } else {
            long startTime = System.currentTimeMillis();
            log.info("pruneCars : Pruning {} cars from DB", carsToPrune.size());
            boolean pruned = false;
            try {
                carModelRepository.deleteAllById(carsToPrune.stream().map(CarModel::getId).toList());
                pruned = true;
            } catch (Exception e) {
                pruned = false;
            } finally {
                if (!pruned) {
                    log.warn("Pruning cars to from failed. Pushing back to pruning list.");
                    synchronized (carPruningList) {
                        carPruningList.addAll(carsToPrune);
                    }
                } else {
                    log.info("pruneCars : Pruned {} cars from DB in {}ms", carsToPrune.size(), System.currentTimeMillis() - startTime);
                }
            }
        }
    }

    private void updateMakeModel(List<CarModel> savedCarModels) {
        if (savedCarModels.isEmpty()) {
            return;
        }
        log.debug("updateMakeModel : Updating make models");
        List<MakeModel> makesToSave = getMakesToSave(savedCarModels);
        List<MakeModel> savedMakes = makeModelRepository.saveAll(makesToSave);
        log.debug("updateMakeModel : Saved {} makes to DB", savedMakes.size());
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

    protected CarModel fetchCarDetailsFromDb(String clientId) {
        log.trace("Checking if car detail fetched for {}", clientId);
        CarModel carModel = new CarModel();
        carModel.setId(clientId);
        log.trace("Adding car {} to list", carModel.getId());
        synchronized (carCheckList) {
            carCheckList.add(carModel);
        }
        log.trace("Added car {} to list", carModel.getId());
        checkForCarList();
        log.trace("Getting fetch details for {}", clientId);
        return fetch(carModel, false);
    }

    @Async
    public void checkForCarList() {
        log.debug("Checking for car list");
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        synchronized (carCheckList) {
            if (carCheckList.isEmpty()) {
                log.debug("No car to check");
                return;
            }
            log.debug("Checking for car list : {}", carCheckList.size());
            List<CarModel> carCheckedList = Collections.synchronizedList(new ArrayList<>());
            List<CarModel> carsFromDb = carModelRepository.findAllById(carCheckList.stream().map(CarModel::getId).toList());
            carCheckList.forEach(carCheck -> {
                CarModel foundCar = carsFromDb.stream().filter(car -> carCheck.getId().equals(car.getId())).findFirst().orElse(null);
                if (foundCar != null) {
                    carExistList.add(foundCar);
                } else {
                    carNotExistList.add(carCheck);
                }
                carCheckedList.add(carCheck);
            });
            log.debug("Checked for car list : {}", carCheckList.size());
            carCheckList.removeAll(carCheckedList);
            log.debug("Remaining car list : {}", carCheckList.size());
        }
        log.debug("Checked for car list");
    }

    private CarModel fetch(CarModel carModel, boolean timeout) {
        long fetchTimeout = 20000;
        long start = System.currentTimeMillis();
        CarModel fetchedCar = null;
        try {
            int fetchStatus = 0;
            while (fetchStatus == 0 && (!timeout || System.currentTimeMillis() - start < fetchTimeout)) {
                synchronized (carExistList) {
                    synchronized (carNotExistList) {
                        if (carExistList.stream().anyMatch(car -> carModel.getId().equals(car.getId()))) {
                            fetchStatus = 1;
                            fetchedCar = carExistList.stream().filter(car -> carModel.getId().equals(car.getId())).findFirst().orElse(null);
                            carExistList.removeAll(carExistList.stream().filter(car -> carModel.getId().equals(car.getId())).toList());
                        } else if (carNotExistList.stream().anyMatch(car -> carModel.getId().equals(car.getId()))) {
                            fetchStatus = -1;
                            carNotExistList.removeAll(carNotExistList.stream().filter(car -> carModel.getId().equals(car.getId())).toList());
                        }
                    }
                }
                if (fetchStatus == 0) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            if (fetchStatus == 0) {
                log.warn("Car detail fetch timeout for " + carModel.getId());
            }
        } catch (Exception e) {
            log.error("Error checking car detail fetched", e);
        }
        return fetchedCar;
    }

    public String startFixThread() {
        log.info("Starting to fix cars from {}", getClientName());
        if (fixThread == null || !fixThread.isAlive()) {
            fixThread = new Thread(() -> {
                try {
                    log.info("Fixing cars from {}", getClientName());
                    long startTime = System.currentTimeMillis();
                    fixAllCars();
                    log.info("Fixed cars from {} in {}ms", getClientName(), System.currentTimeMillis() - startTime);
                } catch (Exception e) {
                    log.error("Error fixing cars for {}", getClientName(), e);
                }
            });
            fixThread.setName("fixThread-" + getClientId());
            fixThread.start();
            return "Started fixing cars from " + getClientName();
        } else {
            return "Already fixing cars from " + getClientName();
        }
    }

    public String startCleanupThread() {
        log.info("Starting to cleanup data from {}", getClientName());
        if (cleanupThread == null || !cleanupThread.isAlive()) {
            cleanupThread = new Thread(() -> {
                try {
                    log.info("Cleaning up data from {}", getClientName());
                    long startTime = System.currentTimeMillis();
                    cleanupData();
                    log.info("Cleaned up data from {} in {}ms", getClientName(), System.currentTimeMillis() - startTime);
                } catch (Exception e) {
                    log.error("Error cleaning up data for {}", getClientName(), e);
                }
            });
            cleanupThread.setName("cleanupThread-" + getClientId());
            cleanupThread.start();
            return "Started cleaning up data from " + getClientName();
        } else {
            return "Already cleaning up data from " + getClientName();
        }
    }
}
