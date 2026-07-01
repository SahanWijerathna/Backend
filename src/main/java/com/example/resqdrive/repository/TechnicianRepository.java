package com.resqdrive.backend.repository;

import com.resqdrive.backend.model.Technician;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TechnicianRepository extends JpaRepository<Technician, String> {
    List<Technician> findByGarageId(String garageId);
    void deleteByGarageId(String garageId);
}
