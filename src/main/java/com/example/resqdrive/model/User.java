package com.example.resqdrive.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    private String id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role; // 'user', 'garage', 'towing', 'admin'

    @Column(nullable = false)
    private String name;

    private String phone;

    // Garage specific fields
    private String ownerName;
    private String address;
    private String hours;
    private String coverageRadius;

    // Towing / Garage specific rates
    private Double ratePerKM;

    // Operator name for Towing
    private String operatorName;

    // Truck Plate for Towing
    private String truckPlate;

    // GPS fields
    private Double gpsLat;
    private Double gpsLng;

    private Boolean suspended = false;

    private Boolean deleted = false; // Soft delete flag

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Vehicle> vehicles = new ArrayList<>();

    @OneToMany(mappedBy = "garage", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<ServiceEntity> services = new ArrayList<>();

    @OneToMany(mappedBy = "garage", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Technician> technicians = new ArrayList<>();

    public User() {}

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

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

    public Double getGpsLat() { return gpsLat; }
    public void setGpsLat(Double gpsLat) { this.gpsLat = gpsLat; }

    public Double getGpsLng() { return gpsLng; }
    public void setGpsLng(Double gpsLng) { this.gpsLng = gpsLng; }

    public Boolean getSuspended() { return suspended; }
    public void setSuspended(Boolean suspended) { this.suspended = suspended; }

    public Boolean getDeleted() { return deleted; }
    public void setDeleted(Boolean deleted) { this.deleted = deleted; }

    public List<Vehicle> getVehicles() { return vehicles; }
    public void setVehicles(List<Vehicle> vehicles) { this.vehicles = vehicles; }

    public List<ServiceEntity> getServices() { return services; }
    public void setServices(List<ServiceEntity> services) { this.services = services; }

    public List<Technician> getTechnicians() { return technicians; }
    public void setTechnicians(List<Technician> technicians) { this.technicians = technicians; }
}
