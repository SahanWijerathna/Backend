package com.resqdrive.backend.repository;

import com.resqdrive.backend.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {
    List<Transaction> findByUserIdOrderByDateDesc(String userId);
    List<Transaction> findByGarageIdOrderByDateDesc(String garageId);
    List<Transaction> findAllByOrderByDateDesc();
}
