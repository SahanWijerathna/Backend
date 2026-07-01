package com.resqdrive.backend.dto;

import com.resqdrive.backend.model.RequestEntity;

public class RequestResponseDto {

    private String id;
    private String userId;
    private String userName;
    private String userPhone;
    private VehicleDto vehicle;
    private String category;
    private String symptoms;
    private String description;
    private String location;
    private GpsDto gps;
    private Boolean imageSimulated;
    private Boolean audioSimulated;
    private String status;
    private String paymentStatus;
    private String garageId;
    private String garageName;
    private String towingId;
    private String towingName;
    private TechnicianDto technician;
    private String eta;
    private String fee;
    private Double dispatchCost;
    private Double platformCommission;
    private Double providerShare;
    private Double distance;
    private String destination;
    private GpsDto destinationGps;
    private Boolean isTowingRequest;
    private String timestamp;

    public RequestResponseDto(RequestEntity req) {
        this.id = req.getId();
        this.userId = req.getUserId();
        this.userName = req.getUserName();
        this.userPhone = req.getUserPhone();
        
        this.vehicle = new VehicleDto(
            req.getVehicleMake(),
            req.getVehicleModel(),
            req.getVehicleYear(),
            req.getVehiclePlate(),
            req.getVehicleInsurance()
        );
        
        this.category = req.getCategory();
        this.symptoms = req.getSymptoms();
        this.description = req.getDescription();
        this.location = req.getLocation();
        
        if (req.getGpsLat() != null && req.getGpsLng() != null) {
            this.gps = new GpsDto(req.getGpsLat(), req.getGpsLng());
        }
        
        this.imageSimulated = req.getImageSimulated();
        this.audioSimulated = req.getAudioSimulated();
        this.status = req.getStatus();
        this.paymentStatus = req.getPaymentStatus();
        this.garageId = req.getGarageId();
        this.garageName = req.getGarageName();
        this.towingId = req.getTowingId();
        this.towingName = req.getTowingName();
        
        if (req.getTechnicianId() != null) {
            this.technician = new TechnicianDto(
                req.getTechnicianId(),
                req.getTechnicianName(),
                req.getTechnicianPhone()
            );
        }
        
        this.eta = req.getEta();
        this.fee = req.getFee();
        this.dispatchCost = req.getDispatchCost();
        this.platformCommission = req.getPlatformCommission();
        this.providerShare = req.getProviderShare();
        this.distance = req.getDistance();
        this.destination = req.getDestination();
        
        if (req.getDestinationGpsLat() != null && req.getDestinationGpsLng() != null) {
            this.destinationGps = new GpsDto(req.getDestinationGpsLat(), req.getDestinationGpsLng());
        }
        
        this.isTowingRequest = req.getIsTowingRequest();
        this.timestamp = req.getCreatedAt();
    }

    // Inner DTOs
    public static class GpsDto {
        private Double lat;
        private Double lng;
        public GpsDto(Double lat, Double lng) {
            this.lat = lat;
            this.lng = lng;
        }
        public Double getLat() { return lat; }
        public Double getLng() { return lng; }
    }

    public static class VehicleDto {
        private String make;
        private String model;
        private String year;
        private String plate;
        private String insurance;
        public VehicleDto(String make, String model, String year, String plate, String insurance) {
            this.make = make;
            this.model = model;
            this.year = year;
            this.plate = plate;
            this.insurance = insurance;
        }
        public String getMake() { return make; }
        public String getModel() { return model; }
        public String getYear() { return year; }
        public String getPlate() { return plate; }
        public String getInsurance() { return insurance; }
    }

    public static class TechnicianDto {
        private String id;
        private String name;
        private String phone;
        public TechnicianDto(String id, String name, String phone) {
            this.id = id;
            this.name = name;
            this.phone = phone;
        }
        public String getId() { return id; }
        public String getName() { return name; }
        public String getPhone() { return phone; }
    }

    // Getters
    public String getId() { return id; }
    public String getUserId() { return userId; }
    public String getUserName() { return userName; }
    public String getUserPhone() { return userPhone; }
    public VehicleDto getVehicle() { return vehicle; }
    public String getCategory() { return category; }
    public String getSymptoms() { return symptoms; }
    public String getDescription() { return description; }
    public String getLocation() { return location; }
    public GpsDto getGps() { return gps; }
    public Boolean getImageSimulated() { return imageSimulated; }
    public Boolean getAudioSimulated() { return audioSimulated; }
    public String getStatus() { return status; }
    public String getPaymentStatus() { return paymentStatus; }
    public String getGarageId() { return garageId; }
    public String getGarageName() { return garageName; }
    public String getTowingId() { return towingId; }
    public String getTowingName() { return towingName; }
    public TechnicianDto getTechnician() { return technician; }
    public String getEta() { return eta; }
    public String getFee() { return fee; }
    public Double getDispatchCost() { return dispatchCost; }
    public Double getPlatformCommission() { return platformCommission; }
    public Double getProviderShare() { return providerShare; }
    public Double getDistance() { return distance; }
    public String getDestination() { return destination; }
    public GpsDto getDestinationGps() { return destinationGps; }
    public Boolean getIsTowingRequest() { return isTowingRequest; }
    public String getTimestamp() { return timestamp; }
}
