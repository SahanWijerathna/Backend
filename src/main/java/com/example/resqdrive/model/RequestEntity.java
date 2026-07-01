package com.resqdrive.backend.model;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "requests")
public class RequestEntity {

    @Id
    private String id;

    @Column(nullable = false)
    private String userId;

    private String userName;
    private String userPhone;

    // Flattened vehicle fields
    private String vehicleMake;
    private String vehicleModel;
    private String vehicleYear;
    private String vehiclePlate;
    private String vehicleInsurance;

    private String category;

    @Column(length = 2000)
    private String symptoms;

    @Column(length = 2000)
    private String description;

    private String location;

    // Flattened GPS coordinates
    private Double gpsLat;
    private Double gpsLng;

    private Boolean imageSimulated = false;
    private Boolean audioSimulated = false;

    private String status = "pending"; 
    private String paymentStatus = "unpaid"; // 'unpaid', 'paid'

    private String garageId;
    private String garageName;
    private String towingId;
    private String towingName;

    // Flattened technician fields
    private String technicianId;
    private String technicianName;
    private String technicianPhone;

    private Boolean isTowingRequest = false;
    private String eta;
    private String fee;
    private Double dispatchCost;
    private Double platformCommission;
    private Double providerShare;
    private Double distance;
    private String destination;

    // Destination GPS
    private Double destinationGpsLat;
    private Double destinationGpsLng;

    private String createdAt = Instant.now().toString();
    private String updatedAt = Instant.now().toString();

    private Boolean deleted = false; // Soft delete flag

    public RequestEntity() {}

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getUserPhone() { return userPhone; }
    public void setUserPhone(String userPhone) { this.userPhone = userPhone; }

    public String getVehicleMake() { return vehicleMake; }
    public void setVehicleMake(String vehicleMake) { this.vehicleMake = vehicleMake; }

    public String getVehicleModel() { return vehicleModel; }
    public void setVehicleModel(String vehicleModel) { this.vehicleModel = vehicleModel; }

    public String getVehicleYear() { return vehicleYear; }
    public void setVehicleYear(String vehicleYear) { this.vehicleYear = vehicleYear; }

    public String getVehiclePlate() { return vehiclePlate; }
    public void setVehiclePlate(String vehiclePlate) { this.vehiclePlate = vehiclePlate; }

    public String getVehicleInsurance() { return vehicleInsurance; }
    public void setVehicleInsurance(String vehicleInsurance) { this.vehicleInsurance = vehicleInsurance; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getSymptoms() { return symptoms; }
    public void setSymptoms(String symptoms) { this.symptoms = symptoms; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Double getGpsLat() { return gpsLat; }
    public void setGpsLat(Double gpsLat) { this.gpsLat = gpsLat; }

    public Double getGpsLng() { return gpsLng; }
    public void setGpsLng(Double gpsLng) { this.gpsLng = gpsLng; }

    public Boolean getImageSimulated() { return imageSimulated; }
    public void setImageSimulated(Boolean imageSimulated) { this.imageSimulated = imageSimulated; }

    public Boolean getAudioSimulated() { return audioSimulated; }
    public void setAudioSimulated(Boolean audioSimulated) { this.audioSimulated = audioSimulated; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public String getGarageId() { return garageId; }
    public void setGarageId(String garageId) { this.garageId = garageId; }

    public String getGarageName() { return garageName; }
    public void setGarageName(String garageName) { this.garageName = garageName; }

    public String getTowingId() { return towingId; }
    public void setTowingId(String towingId) { this.towingId = towingId; }

    public String getTowingName() { return towingName; }
    public void setTowingName(String towingName) { this.towingName = towingName; }

    public String getTechnicianId() { return technicianId; }
    public void setTechnicianId(String technicianId) { this.technicianId = technicianId; }

    public String getTechnicianName() { return technicianName; }
    public void setTechnicianName(String technicianName) { this.technicianName = technicianName; }

    public String getTechnicianPhone() { return technicianPhone; }
    public void setTechnicianPhone(String technicianPhone) { this.technicianPhone = technicianPhone; }

    public Boolean getIsTowingRequest() { return isTowingRequest; }
    public void setIsTowingRequest(Boolean isTowingRequest) { this.isTowingRequest = isTowingRequest; }

    public String getEta() { return eta; }
    public void setEta(String eta) { this.eta = eta; }

    public String getFee() { return fee; }
    public void setFee(String fee) { this.fee = fee; }

    public Double getDispatchCost() { return dispatchCost; }
    public void setDispatchCost(Double dispatchCost) { this.dispatchCost = dispatchCost; }

    public Double getPlatformCommission() { return platformCommission; }
    public void setPlatformCommission(Double platformCommission) { this.platformCommission = platformCommission; }

    public Double getProviderShare() { return providerShare; }
    public void setProviderShare(Double providerShare) { this.providerShare = providerShare; }

    public Double getDistance() { return distance; }
    public void setDistance(Double distance) { this.distance = distance; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public Double getDestinationGpsLat() { return destinationGpsLat; }
    public void setDestinationGpsLat(Double destinationGpsLat) { this.destinationGpsLat = destinationGpsLat; }

    public Double getDestinationGpsLng() { return destinationGpsLng; }
    public void setDestinationGpsLng(Double destinationGpsLng) { this.destinationGpsLng = destinationGpsLng; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }

    public Boolean getDeleted() { return deleted; }
    public void setDeleted(Boolean deleted) { this.deleted = deleted; }
}
