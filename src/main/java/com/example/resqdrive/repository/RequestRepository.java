package com.example.resqdrive.repository;

import com.example.resqdrive.model.RequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<RequestEntity, String>, JpaSpecificationExecutor<RequestEntity> {
    
    List<RequestEntity> findByUserIdOrderByCreatedAtDesc(String userId);

    @Query("SELECT r FROM RequestEntity r WHERE r.garageId = :garageId OR " +
           "(r.garageId IS NULL AND r.status = 'pending' AND r.towingId IS NULL AND (r.isTowingRequest = false OR r.isTowingRequest IS NULL)) " +
           "ORDER BY r.createdAt DESC")
    List<RequestEntity> findForGarage(@Param("garageId") String garageId);

    @Query("SELECT r FROM RequestEntity r WHERE r.towingId = :towingId OR " +
           "(r.towingId IS NULL AND r.status = 'pending' AND (r.isTowingRequest = true OR r.category = 'Accident')) " +
           "ORDER BY r.createdAt DESC")
    List<RequestEntity> findForTowing(@Param("towingId") String towingId);

    List<RequestEntity> findAllByOrderByCreatedAtDesc();
}
