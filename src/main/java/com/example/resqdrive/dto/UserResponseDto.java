package com.example.resqdrive.dto;

import com.example.resqdrive.model.User;
import com.example.resqdrive.model.Vehicle;
import com.example.resqdrive.model.ServiceEntity;
import com.example.resqdrive.model.Technician;
import java.util.List;

public class UserResponseDto {

    private String id;
    private String email;
    private String role;
    private String name;
    private String phone;
    private String ownerName;
    private String address;
    private String hours;
    private String coverageRadius;
    private Double ratePerKM;
    private String operatorName;
    private String truckPlate;
    private GpsDto gps;
    private Boolean suspended;

    private List<Vehicle> vehicles;
    private List<ServiceEntity> services;
    private List<Technician> technicians;

    public UserResponseDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.name = user.getName();
        this.phone = user.getPhone();
        this.ownerName = user.getOwnerName();
        this.address = user.getAddress();
        this.hours = user.getHours();
        this.coverageRadius = user.getCoverageRadius();
        this.ratePerKM = user.getRatePerKM();
        this.operatorName = user.getOperatorName();
        this.truckPlate = user.getTruckPlate();
        
        if (user.getGpsLat() != null && user.getGpsLng() != null) {
            this.gps = new GpsDto(user.getGpsLat(), user.getGpsLng());
        }
        
        this.suspended = user.getSuspended();
        this.vehicles = user.getVehicles();
        this.services = user.getServices();
        this.technicians = user.getTechnicians();
    }

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

    // Getters
    public String getId() { return id; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getOwnerName() { return ownerName; }
    public String getAddress() { return address; }
    public String getHours() { return hours; }
    public String getCoverageRadius() { return coverageRadius; }
    public Double getRatePerKM() { return ratePerKM; }
    public String getOperatorName() { return operatorName; }
    public String getTruckPlate() { return truckPlate; }
    public GpsDto getGps() { return gps; }
    public Boolean getSuspended() { return suspended; }
    public List<Vehicle> getVehicles() { return vehicles; }
    public List<ServiceEntity> getServices() { return services; }
    public List<Technician> getTechnicians() { return technicians; }
}
