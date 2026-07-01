package com.example.resqdrive.repository;

import com.example.resqdrive.model.ServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<ServiceEntity, String> {
    List<ServiceEntity> findByGarageId(String garageId);
    void deleteByGarageId(String garageId);
}
