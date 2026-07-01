package com.resqdrive.backend.dto;

public class RequestCreateDto {

    private String userName;
    private String userPhone;
    private VehicleDto vehicle;
    private String category;
    private String symptoms;
    private String description;
    private String location;
    private GpsDto gps;
    private Boolean imageSimulated = false;
    private Boolean audioSimulated = false;
    private Boolean isTowingRequest = false;
    private String destination;
    private GpsDto destinationGps;

    public RequestCreateDto() {}

    public static class GpsDto {
        private Double lat;
        private Double lng;
        public GpsDto() {}
        public Double getLat() { return lat; }
        public void setLat(Double lat) { this.lat = lat; }
        public Double getLng() { return lng; }
        public void setLng(Double lng) { this.lng = lng; }
    }

    public static class VehicleDto {
        private String make;
        private String model;
        private String year;
        private String plate;
        private String insurance;
        public VehicleDto() {}
        public String getMake() { return make; }
        public void setMake(String make) { this.make = make; }
        public String getModel() { return model; }
        public void setModel(String model) { this.model = model; }
        public String getYear() { return year; }
        public void setYear(String year) { this.year = year; }
        public String getPlate() { return plate; }
        public void setPlate(String plate) { this.plate = plate; }
        public String getInsurance() { return insurance; }
        public void setInsurance(String insurance) { this.insurance = insurance; }
    }

    // Getters and Setters
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getUserPhone() { return userPhone; }
    public void setUserPhone(String userPhone) { this.userPhone = userPhone; }

    public VehicleDto getVehicle() { return vehicle; }
    public void setVehicle(VehicleDto vehicle) { this.vehicle = vehicle; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getSymptoms() { return symptoms; }
    public void setSymptoms(String symptoms) { this.symptoms = symptoms; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public GpsDto getGps() { return gps; }
    public void setGps(GpsDto gps) { this.gps = gps; }

    public Boolean getImageSimulated() { return imageSimulated; }
    public void setImageSimulated(Boolean imageSimulated) { this.imageSimulated = imageSimulated; }

    public Boolean getAudioSimulated() { return audioSimulated; }
    public void setAudioSimulated(Boolean audioSimulated) { this.audioSimulated = audioSimulated; }

    public Boolean getIsTowingRequest() { return isTowingRequest; }
    public void setIsTowingRequest(Boolean isTowingRequest) { this.isTowingRequest = isTowingRequest; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public GpsDto getDestinationGps() { return destinationGps; }
    public void setDestinationGps(GpsDto destinationGps) { this.destinationGps = destinationGps; }
}
