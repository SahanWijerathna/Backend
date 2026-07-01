package com.resqdrive.backend.controller;

import com.resqdrive.backend.config.JwtFilter.CustomPrincipal;
import com.resqdrive.backend.model.RequestEntity;
import com.resqdrive.backend.model.Transaction;
import com.resqdrive.backend.repository.RequestRepository;
import com.resqdrive.backend.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.*;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private RequestRepository requestRepository;

    @GetMapping
    public ResponseEntity<?> getTransactions() {
        CustomPrincipal principal = (CustomPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String role = principal.getRole();
        String id = principal.getId();

        List<Transaction> txns;

        if ("admin".equals(role)) {
            txns = transactionRepository.findAllByOrderByDateDesc();
        } else if ("user".equals(role)) {
            txns = transactionRepository.findByUserIdOrderByDateDesc(id);
        } else if ("garage".equals(role) || "towing".equals(role)) {
            txns = transactionRepository.findByGarageIdOrderByDateDesc(id);
        } else {
            txns = Collections.emptyList();
        }

        return ResponseEntity.ok(txns);
    }

    @PostMapping
    public ResponseEntity<?> createTransaction(@RequestBody Map<String, String> body) {
        String requestId = body.get("requestId");
        String paymentMethod = body.get("paymentMethod");

        if (requestId == null || requestId.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Request ID is required."));
        }

        Optional<RequestEntity> reqOpt = requestRepository.findById(requestId);
        if (reqOpt.isEmpty() || reqOpt.get().getDeleted()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Request not found."));
        }

        RequestEntity request = reqOpt.get();
        request.setPaymentStatus("paid");
        request.setUpdatedAt(Instant.now().toString());
        requestRepository.save(request);

        String txnId = "TXN-" + (100000 + new Random().nextInt(900000));
        Transaction txn = new Transaction();
        txn.setId(txnId);
        txn.setRequestId(request.getId());
        txn.setUserId(request.getUserId());
        txn.setUserName(request.getUserName());
        txn.setGarageId(request.getGarageId() != null ? request.getGarageId() : request.getTowingId());
        txn.setGarageName(request.getGarageName() != null ? request.getGarageName() : (request.getTowingName() != null ? request.getTowingName() : "VAMP Platform"));
        
        String vehicleInfo = (request.getVehicleMake() != null ? request.getVehicleMake() : "") + " " + 
                             (request.getVehicleModel() != null ? request.getVehicleModel() : "") + " (" + 
                             (request.getVehiclePlate() != null ? request.getVehiclePlate() : "") + ")";
        txn.setVehicle(vehicleInfo.trim());
        txn.setAmount(request.getFee() != null ? request.getFee() : "$0.00");
        txn.setPaymentMethod(paymentMethod != null ? paymentMethod : "Visa ending 4521");
        txn.setStatus("Successful");
        txn.setDate(Instant.now().toString());

        transactionRepository.save(txn);

        return ResponseEntity.status(HttpStatus.CREATED).body(txn);
    }
}
