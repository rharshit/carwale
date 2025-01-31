package com.rharshit.carsync.service.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rharshit.carsync.repository.model.CarModel;
import com.rharshit.carsync.repository.model.client.CarWaleCarModel;
import com.rharshit.carsync.service.ClientService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@Service
public class CarWaleService extends ClientService<CarWaleCarModel> {

    Set<AllCarResponse.Stock> allStocks = Collections.synchronizedSet(new HashSet<>());

    // TODO: Implement this method
    @Override
    public void fetchAllCars() {
        long start = System.currentTimeMillis();
        ExecutorService discoveryExecutor = Executors.newFixedThreadPool(64);
        ExecutorService fetchExecutor = Executors.newFixedThreadPool(64);

        List<Integer> cities = getCityList();
        for (int city : cities) {
            discoveryExecutor.execute(() -> {
                log.trace("Fetching cars for city : " + city);
                long startTime = System.currentTimeMillis();
                fetchCarsForCity(city, fetchExecutor);
                log.trace("Fetched cars for city : " + city + " in " + (System.currentTimeMillis() - startTime) + "ms");
            });
        }
        discoveryExecutor.shutdown();
        try {
            while (!discoveryExecutor.awaitTermination(1000, java.util.concurrent.TimeUnit.NANOSECONDS)) {
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            log.error("Error waiting for discovery executor to finish", e);
        }
        log.info("Fetched list of all cars from CarWale");

        fetchExecutor.shutdown();
        try {
            while (!fetchExecutor.awaitTermination(1000, java.util.concurrent.TimeUnit.NANOSECONDS)) {
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            log.error("Error waiting for fetch executor to finish", e);
        }
        log.info("Fetched all car details from CarWale");
        log.info("Total cars fetched : " + allStocks.size() + " in " + (System.currentTimeMillis() - start) + "ms");
    }

    private List<Integer> getCityList() {
        CityListResponse cityListResponse = getRestClient().post().uri("/api/used-search/filters/")
                .contentType(APPLICATION_JSON).body("{}")
                .retrieve().body(CityListResponse.class);
        if (cityListResponse == null || cityListResponse.city == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(cityListResponse.city.stream().map(city -> city.cityId).collect(Collectors.toSet()));
    }

    private void fetchCarsForCity(int city, ExecutorService fetchExecutor) {
        List<AllCarResponse.Stock> stocks = new ArrayList<>();
        List<AllCarResponse.Stock> currentStocks;
        int total;
        int fetched;
        AllCarResponse response = getRestClient().post().uri("/api/stocks/filters/")
                .contentType(APPLICATION_JSON).body("{\"city\":\"" + city + "\",\"pn\":\"1\",\"ps\":\"1000\"}")
                .retrieve().body(AllCarResponse.class);
        total = response.totalCount;
        currentStocks = response.stocks;
        stocks.addAll(currentStocks);
        fetchDetails(currentStocks, fetchExecutor);
        fetched = stocks.size();
        while (!response.stocks.isEmpty() && response.nextPageUrl != null) {
            log.info((int) getPercentage(total, fetched) + "% : Fetched " + fetched + " cars out of " + total + " from CarWale");
            response = getRestClient().get().uri(response.nextPageUrl)
                    .retrieve().body(AllCarResponse.class);
            total = response.totalCount;
            currentStocks = response.stocks;
            stocks.addAll(currentStocks);
            fetchDetails(response.stocks, fetchExecutor);
            fetched = stocks.size();
            log.info("Stock size : " + stocks.size());
            log.info("Next page  : " + response.nextPageUrl);
            if (response.nextPageUrl != null) {
                log.info("Fetched    : " + response.nextPageUrl.split("stockfetched=")[1].split("&")[0]);
            } else {
                log.info("No next page");
            }
        }
        allStocks.addAll(stocks);
        log.info("Fetched a total of " + stocks.size() + " cars of " + total + " cars from CarWale");
    }

    private void fetchDetails(List<AllCarResponse.Stock> currentStocks, ExecutorService fetchExecutor) {
        currentStocks.forEach(stock -> fetchExecutor.execute(() -> fetchStockDetails(stock)));
    }

    private void fetchStockDetails(AllCarResponse.Stock stock) {
        try {
            log.trace("Fetching details for car : " + stock.makeName + " " + stock.modelName + " " + stock.versionName);
            long startTime = System.currentTimeMillis();
            CarWaleCarModel carModel = new CarWaleCarModel();
            populateCarModel(stock, carModel);

            String response = getRestClient().get().uri(stock.url).retrieve().body(String.class);
            if (response == null) {
                log.info("Error fetching details for car : " + stock.makeName + " " + stock.modelName + " " + stock.versionName + " " + stock.url + ". Retrying...");
                response = getRestClient().get().uri(stock.url).retrieve().body(String.class);
                if (response == null) {
                    log.error("Error fetching details for car : " + stock.makeName + " " + stock.modelName + " " + stock.versionName + " " + stock.url + ". Giving up...");
                    return;
                }
            }
            Document doc = Jsoup.parse(response);
            Elements specPairs = doc.getElementsByTag("ul");

            for (Element specPair : specPairs) {
                Elements specElements = specPair.getElementsByTag("li");
                if (specElements.size() == 2) {
                    List<String> specs = specElements.stream().map(Element::text).toList();
                    populateSpecs(carModel.getSpecs(), specs);
                }
            }
            log.trace("Fetched details for car : " + carModel.getMake() + " " + carModel.getModel() + " " + carModel.getVariant() + " in " + (System.currentTimeMillis() - startTime) + "ms");
        } catch (Exception e) {
            log.error("Error fetching details for car : " + stock.makeName + " " + stock.modelName + " " + stock.versionName + " " + stock.url, e);
        }

    }

    private void populateCarModel(AllCarResponse.Stock stock, CarWaleCarModel carModel) {
        carModel.setMake(stock.makeName);
        carModel.setModel(stock.modelName);
        carModel.setVariant(stock.versionName);
        carModel.setYear(stock.makeYear);
        carModel.setPrice(Integer.parseInt(stock.priceNumeric));
        carModel.setMileage(Integer.parseInt(stock.kmNumeric));
        carModel.setInternalId(stock.profileId);
    }

    private void populateSpecs(CarModel.Specs carSpecs, List<String> webSpecs) {
        switch (webSpecs.getFirst()) {
            case "Engine":
                carSpecs.setEngineType(webSpecs.getLast());
                try {
                    carSpecs.setEngineDisplacement(Integer.parseInt(webSpecs.getLast().split(",")[0].split("cc")[0].strip()));
                } catch (Exception e) {
                }
                break;
            case "Fuel Type":
                carSpecs.setFuelType(webSpecs.getLast());
                break;
            case "Max Power (bhp@rpm)":
                try {
                    carSpecs.setEnginePower(Integer.parseInt(webSpecs.getLast().split("@")[0].split("bhp")[0].strip()));
                } catch (Exception e) {
                }
                break;
            case "Max Torque (Nm@rpm)":
                try {
                    carSpecs.setEngineTorque(Integer.parseInt(webSpecs.getLast().split("@")[0].split("Nm")[0].strip()));
                } catch (Exception e) {
                }
                break;
            case "Drivetrain":
                carSpecs.setDrivetrain(webSpecs.getLast());
                break;
            case "Transmission":
                carSpecs.setTransmissionType(webSpecs.getLast());
                break;
            case "Length":
                try {
                    carSpecs.setLength(Integer.parseInt(webSpecs.getLast().split("mm")[0].strip()));
                } catch (Exception e) {
                }
                break;
            case "Width":
                try {
                    carSpecs.setWidth(Integer.parseInt(webSpecs.getLast().split("mm")[0].strip()));
                } catch (Exception e) {
                }
                break;
            case "Height":
                try {
                    carSpecs.setHeight(Integer.parseInt(webSpecs.getLast().split("mm")[0].strip()));
                } catch (Exception e) {
                }
                break;
            case "Wheelbase":
                try {
                    carSpecs.setWheelbase(Integer.parseInt(webSpecs.getLast().split("mm")[0].strip()));
                } catch (Exception e) {
                }
                break;
            case "Ground Clearance":
                try {
                    carSpecs.setGroundClearance(Integer.parseInt(webSpecs.getLast().split("mm")[0].strip()));
                } catch (Exception e) {
                }
                break;
            case "Kerb Weight":
                try {
                    carSpecs.setKerbWeight(Integer.parseInt(webSpecs.getLast().split("kg")[0].strip()));
                } catch (Exception e) {
                }
                break;
        }
    }

    @Override
    public String getClientId() {
        return "carwale";
    }

    @Override
    public String getClientName() {
        return "CarWale";
    }

    @Override
    public String getClientDomain() {
        return "https://www.carwale.com";
    }

    static class AllCarResponse {
        public List<Stock> stocks;
        public String nextPageUrl;
        public int totalCount;
        public boolean showSimilarCarsLink;
        public String searchPageDescription;

        static class Stock {
            public String profileId;
            public String carName;
            public int dealerId;
            public int rootId;
            public String rootName;
            public String url;
            public String nearbyCityText;
            public boolean isNearbyCityListing;
            public String valuationUrl;
            public int makeYear;
            public String price;
            public String priceNumeric;
            public String km;
            public String kmNumeric;
            public String fuel;
            public String areaName;
            public int cityId;
            public String cityName;
            public String emiFormatted;
            public String financeUrl;
            public String financeUrlText;
            public boolean isEligibleForFinance;
            public boolean isPremium;
            public int certificationId;
            public String imageUrl;
            public int deliveryCity;
            public int certProgId;
            public String certProgLogoUrl;
            public String similarCarsUrl;
            public String dealerCarsUrl;
            public boolean shouldShowBreaker;
            public int nearbyCarsBucket;
            public int cwBasePackageId;
            public int ctePackageId;
            public int slotId;
            public String additionalFuel;
            public String oemVehicleUrl;
            public String makeId;
            public String bodyStyleId;
            public String makeName;
            public String modelName;
            public List<String> stockImages;
            public String dealershipLogoUrl;
            public int bookingStatus;
            public String virtualPhoneNumber;
            public String carValuationLink;
            public String versionName;
            public List<AdditionalTag> additionalTags;
            public String stockId;
            public String transmission;
            public String modelId;
            public int versionId;
            public int ownersId;
            public int warrantyType;
            public int sellerType;
            public String sellerName;
            public String tagText;
            public String emiText;
            public String emiCtaText;
            public EmiDetail emiDetail;
            public boolean isTrusted;
            public int slotType;
            public int legitimacyStatus;
            public int trimMasterId;
            public String trimName;
            public double certificationScore;
            public String formattedOriginalPrice;
            public String formattedDiscountPrice;
            public CampaignTagDetails campaignTagDetails;
            public String specialOfferText;

            @Override
            public boolean equals(Object o) {
                if (o == null || getClass() != o.getClass()) return false;
                Stock stock = (Stock) o;
                return Objects.equals(profileId, stock.profileId) && Objects.equals(url, stock.url);
            }

            @Override
            public int hashCode() {
                return Objects.hash(profileId, url);
            }
        }

        static class AdditionalTag {
            public int id;
            public String name;
        }

        static class CampaignTagDetails {
            public String text;
            public String color;
            public boolean isActive;
        }

        static class DownPayment {
            public double minimum;
            public double maximum;
            @JsonProperty("default")
            public double mydefault;
        }

        static class EmiDetail {
            public DownPayment downPayment;
            public Interest interest;
            public Tenure tenure;
        }

        static class Interest {
            public double minimum;
            public double maximum;
            @JsonProperty("default")
            public double mydefault;
        }

        static class Tenure {
            public int minimum;
            public int maximum;
            @JsonProperty("default")
            public int mydefault;
        }
    }

    static class CityListResponse {
        public List<City> city;

        static class City {
            public int cityId;
            public int cityCount;
            public String cityName;
            public String cityMaskingName;
        }
    }
}
