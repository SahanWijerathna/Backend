package com.example.resqdrive.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

public class RegisterRequest {

    @NotBlank(message = "Full name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    private String phone;

    @NotBlank(message = "Role choice is required")
    private String role;

    // Optional fields depending on role
    private String garageName;
    private String companyName;
    private String ownerName;
    private String address;
    private String hours;
    private String coverageRadius;
    private Double ratePerKM;
    private String operatorName;
    private String truckPlate;
    private GpsDto gps;

    private List<VehicleDto> vehicles;
    private List<ServiceDto> services;
    private List<TechnicianDto> technicians;

    public RegisterRequest() {}

    // Inner DTOs
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
        private String id;
        private String make;
        private String model;
        private String year;
        private String plate;
        private String insurance;
        public VehicleDto() {}
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
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

    public static class ServiceDto {
        private String id;
        private String name;
        private String price;
        private String desc;
        public ServiceDto() {}
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getPrice() { return price; }
        public void setPrice(String price) { this.price = price; }
        public String getDesc() { return desc; }
        public void setDesc(String desc) { this.desc = desc; }
    }

    public static class TechnicianDto {
        private String id;
        private String name;
        private String phone;
        private String status;
        public TechnicianDto() {}
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getGarageName() { return garageName; }
    public void setGarageName(String garageName) { this.garageName = garageName; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getHours() { return hours; }
    public void setHours(String hours) { this.hours = hours; }

    public String getCoverageRadius() { return coverageRadius; }
    public void setCoverageRadius(String coverageRadius) { this.coverageRadius = coverageRadius; }

    public Double getRatePerKM() { return ratePerKM; }
    public void setRatePerKM(Double ratePerKM) { this.ratePerKM = ratePerKM; }

    public String getOperatorName() { return operatorName; }
    public void setOperatorName(String operatorName) { this.operatorName = operatorName; }

    public String getTruckPlate() { return truckPlate; }
    public void setTruckPlate(String truckPlate) { this.truckPlate = truckPlate; }

    public GpsDto getGps() { return gps; }
    public void setGps(GpsDto gps) { this.gps = gps; }

    public List<VehicleDto> getVehicles() { return vehicles; }
    public void setVehicles(List<VehicleDto> vehicles) { this.vehicles = vehicles; }

    public List<ServiceDto> getServices() { return services; }
    public void setServices(List<ServiceDto> services) { this.services = services; }

    public List<TechnicianDto> getTechnicians() { return technicians; }
    public void setTechnicians(List<TechnicianDto> technicians) { this.technicians = technicians; }
}
