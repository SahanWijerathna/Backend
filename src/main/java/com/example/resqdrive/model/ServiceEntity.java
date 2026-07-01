package com.resqdrive.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "services")
public class ServiceEntity {

    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "garage_id", nullable = false)
    @JsonBackReference
    private User garage;

    private String name;
    private String price;

    @Column(name = "description", length = 1000)
    private String desc;

    public ServiceEntity() {}

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public User getGarage() { return garage; }
    public void setGarage(User garage) { this.garage = garage; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPrice() { return price; }
    public void setPrice(String price) { this.price = price; }

    public String getDesc() { return desc; }
    public void setDesc(String desc) { this.desc = desc; }
}
