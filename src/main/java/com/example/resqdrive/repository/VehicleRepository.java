package com.example.resqdrive.repository;

import com.example.resqdrive.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, String> {
    List<Vehicle> findByUserId(String userId);
    void deleteByUserId(String userId);
}
