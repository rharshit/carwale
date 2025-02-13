package com.rharshit.carsync.service.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rharshit.carsync.common.Utils;
import com.rharshit.carsync.repository.model.CarModel;
import com.rharshit.carsync.repository.model.client.CarWaleCarModel;
import com.rharshit.carsync.service.ClientService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.rharshit.carsync.common.Constants.CLIENT_ID_CARWALE;
import static com.rharshit.carsync.common.Constants.CLIENT_NAME_CARWALE;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@Service
public class CarWaleService extends ClientService<CarWaleCarModel> {

    @Override
    public void fetchAllCars() {
        try (ExecutorService discoveryExecutor = Executors.newFixedThreadPool(64)) {
            try (ExecutorService dbExecutor = Executors.newFixedThreadPool(256)) {
                try (ExecutorService fetchExecutor = Executors.newFixedThreadPool(32)) {
                    List<Integer> cities = getCityList();
                    for (int city : cities) {
                        discoveryExecutor.execute(() -> {
                            log.trace("Fetching cars for city : {}", city);
                            long startTime = System.currentTimeMillis();
                            fetchCarsForCity(city, dbExecutor, fetchExecutor);
                            log.trace("Fetched cars for city : {} in {}ms", city, System.currentTimeMillis() - startTime);
                        });
                    }

                    Utils.awaitShutdownExecutorService(discoveryExecutor);
                    Utils.awaitShutdownExecutorService(dbExecutor);
                    Utils.awaitShutdownExecutorService(fetchExecutor);

                    log.info("Fetched all car details from CarWale");
                }
            }
        }
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

    private void fetchCarsForCity(int city, ExecutorService dbExecutor, ExecutorService fetchExecutor) {
        List<AllCarResponse.Stock> currentStocks;
        int total;
        int fetched;
        AllCarResponse response = getRestClient().post().uri("/api/stocks/filters/")
                .contentType(APPLICATION_JSON).body("{\"city\":\"" + city + "\",\"pn\":\"1\",\"ps\":\"1000\"}")
                .retrieve().body(AllCarResponse.class);
        assert response != null;
        total = response.totalCount;
        currentStocks = response.stocks;
        List<AllCarResponse.Stock> stocks = new ArrayList<>(currentStocks);
        currentStocks.forEach(stock -> dbExecutor.execute(() -> fetchStockDetails(stock, fetchExecutor)));
        fetched = stocks.size();
        while (!response.stocks.isEmpty() && response.nextPageUrl != null) {
            log.trace("{}% : Fetched {} cars out of {} from CarWale", (int) getPercentage(total, fetched), fetched, total);
            response = getRestClient().get().uri(response.nextPageUrl)
                    .retrieve().body(AllCarResponse.class);
            assert response != null;
            total = response.totalCount;
            currentStocks = response.stocks;
            stocks.addAll(currentStocks);
            currentStocks.forEach(stock -> dbExecutor.execute(() -> fetchStockDetails(stock, fetchExecutor)));
            fetched = stocks.size();
            log.trace("Stock size : {}", stocks.size());
            log.trace("Next page  : {}", response.nextPageUrl);
            if (response.nextPageUrl != null) {
                log.trace("Fetched    : {}", response.nextPageUrl.split("stockfetched=")[1].split("&")[0]);
            } else {
                log.trace("No next page");
            }
        }
        log.info("Fetched a list of {} cars CarWale for city {}", stocks.size(), city);
    }

    private void fetchStockDetails(AllCarResponse.Stock stock, ExecutorService fetchExecutor) {
        log.trace("Fetching details for car : {} {} {}", stock.makeName, stock.modelName, stock.versionName);
        CarWaleCarModel carModel = new CarWaleCarModel(stock.profileId);
        populateCarModel(stock, carModel);

        boolean fetched = isCarDetailFetched(carModel.getClientId());
        if (fetched) {
            log.trace("Details already fetched for car : {} {} {}", carModel.getMake(), carModel.getModel(), carModel.getVariant());
            return;
        }
        fetchExecutor.execute(() -> fetchStockDetails(carModel, stock.url));
    }

    private void fetchStockDetails(CarWaleCarModel carModel, String url) {
        try {
            long startTime = System.currentTimeMillis();
            String response = getRestClient().get().uri(url).retrieve().body(String.class);
            if (response == null) {
                log.debug("Error fetching details for car : {} {} {} {}. Retrying...", carModel.getMake(), carModel.getModel(), carModel.getVariant(), url);
                response = getRestClient().get().uri(url).retrieve().body(String.class);
                if (response == null) {
                    log.error("Error fetching details for car : {} {} {} {}. Giving up...", carModel.getMake(), carModel.getModel(), carModel.getVariant(), url);
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
            pushCar(carModel);
            log.trace("Fetched details for car : {} {} {} in {}ms", carModel.getMake(), carModel.getModel(), carModel.getVariant(), System.currentTimeMillis() - startTime);
        } catch (Exception e) {
            log.error("Error fetching details for car : {} {} {} {}", carModel.getMake(), carModel.getModel(), carModel.getVariant(), url, e);
        }

    }

    private void populateCarModel(AllCarResponse.Stock stock, CarWaleCarModel carModel) {
        carModel.setId(carModel.getClientId());
        carModel.setMake(stock.makeName);
        carModel.setModel(stock.modelName);
        carModel.setVariant(stock.versionName);
        carModel.setYear(stock.makeYear);
        carModel.setPrice(Integer.parseInt(stock.priceNumeric));
        carModel.setMileage(Integer.parseInt(stock.kmNumeric));
        carModel.setUrl(getClientDomain() + stock.url);
    }

    private void populateSpecs(CarModel.Specs carSpecs, List<String> webSpecs) {
        try {
            switch (webSpecs.getFirst()) {
                case "Engine":
                    carSpecs.setEngineType(webSpecs.getLast());
                    carSpecs.setEngineDisplacement(Integer.parseInt(webSpecs.getLast().split(",")[0].split("cc")[0].strip()));
                    break;
                case "Fuel Type":
                    carSpecs.setFuelType(webSpecs.getLast());
                    break;
                case "Max Power (bhp@rpm)":
                    carSpecs.setEnginePower(Integer.parseInt(webSpecs.getLast().split("@")[0].split("bhp")[0].strip()));
                    break;
                case "Max Torque (Nm@rpm)":
                    carSpecs.setEngineTorque(Integer.parseInt(webSpecs.getLast().split("@")[0].split("Nm")[0].strip()));
                    break;
                case "Drivetrain":
                    carSpecs.setDrivetrain(webSpecs.getLast());
                    break;
                case "Transmission":
                    carSpecs.setTransmissionType(webSpecs.getLast());
                    break;
                case "Length":
                    carSpecs.setLength(Integer.parseInt(webSpecs.getLast().split("mm")[0].strip()));
                    break;
                case "Width":
                    carSpecs.setWidth(Integer.parseInt(webSpecs.getLast().split("mm")[0].strip()));
                    break;
                case "Height":
                    carSpecs.setHeight(Integer.parseInt(webSpecs.getLast().split("mm")[0].strip()));
                    break;
                case "Wheelbase":
                    carSpecs.setWheelbase(Integer.parseInt(webSpecs.getLast().split("mm")[0].strip()));
                    break;
                case "Ground Clearance":
                    carSpecs.setGroundClearance(Integer.parseInt(webSpecs.getLast().split("mm")[0].strip()));
                    break;
                case "Kerb Weight":
                    carSpecs.setKerbWeight(Integer.parseInt(webSpecs.getLast().split("kg")[0].strip()));
                    break;
            }
        } catch (Exception e) {
            log.trace("Error parsing spec : {}", webSpecs, e);
        }
    }

    @Override
    public String getClientId() {
        return CLIENT_ID_CARWALE;
    }

    @Override
    public String getClientName() {
        return CLIENT_NAME_CARWALE;
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
