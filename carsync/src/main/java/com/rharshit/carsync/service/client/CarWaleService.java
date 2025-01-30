package com.rharshit.carsync.service.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rharshit.carsync.repository.model.client.CarWaleCarModel;
import com.rharshit.carsync.service.ClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@Service
public class CarWaleService extends ClientService<CarWaleCarModel> {

    List<AllCarResponse.Stock> allStocks = new ArrayList<>();

    // TODO: Implement this method
    @Override
    public void fetchAllCars() {
        List<Integer> cities = getCityList();
        for (int city : cities) {
            log.info("Fetching cars for city : " + city);
            fetchCarsForCity(city);
            log.info("Fetched cars for city : " + city);
        }
        log.info("Fetched all cars from CarWale");
        log.info("Total cars fetched : " + allStocks.size());
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

    private void fetchCarsForCity(int city) {
        List<AllCarResponse.Stock> stocks = new ArrayList<>();
        int total;
        int fetched;
        AllCarResponse response = getRestClient().post().uri("/api/stocks/filters/")
                .contentType(APPLICATION_JSON).body("{\"city\":\"" + city + "\",\"pn\":\"1\",\"ps\":\"1000\"}")
                .retrieve().body(AllCarResponse.class);
        total = response.totalCount;
        stocks.addAll(response.stocks);
        fetched = stocks.size();
        while (!response.stocks.isEmpty() && response.nextPageUrl != null) {
            log.info((int) getPercentage(total, fetched) + "% : Fetched " + fetched + " cars out of " + total + " from CarWale");
            response = getRestClient().get().uri(response.nextPageUrl)
                    .retrieve().body(AllCarResponse.class);
            total = response.totalCount;
            stocks.addAll(response.stocks);
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
