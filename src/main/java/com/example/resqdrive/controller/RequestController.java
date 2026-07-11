package com.example.resqdrive.controller;

import com.example.resqdrive.config.JwtFilter.CustomPrincipal;
import com.example.resqdrive.dto.RequestCreateDto;
import com.example.resqdrive.dto.RequestResponseDto;
import com.example.resqdrive.dto.RequestUpdateDto;
import com.example.resqdrive.model.Notification;
import com.example.resqdrive.model.RequestEntity;
import com.example.resqdrive.model.User;
import com.example.resqdrive.repository.NotificationRepository;
import com.example.resqdrive.repository.RequestRepository;
import com.example.resqdrive.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;
import java.util.*;

@RestController
@RequestMapping("/api/requests")
public class RequestController {

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    private double calculateHaversineDistance(Double lat1, Double lng1, Double lat2, Double lng2) {
        if (lat1 == null || lng1 == null || lat2 == null || lng2 == null) {
            return 8.5; // fallback
        }
        double R = 6371; // km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c;
        return Math.round(distance * 10.0) / 10.0;
    }

    @GetMapping
    public ResponseEntity<?> getRequests(@RequestParam(value = "category", required = false) String category) {
        CustomPrincipal principal = (CustomPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String role = principal.getRole();
        String id = principal.getId();

        List<RequestEntity> dbRequests;

        if ("admin".equals(role)) {
            dbRequests = requestRepository.findAllByOrderByCreatedAtDesc();
        } else if ("user".equals(role)) {
            dbRequests = requestRepository.findByUserIdOrderByCreatedAtDesc(id);
        } else if ("garage".equals(role)) {
            dbRequests = requestRepository.findForGarage(id);
        } else if ("towing".equals(role)) {
            dbRequests = requestRepository.findForTowing(id);
        } else {
            dbRequests = Collections.emptyList();
        }

        List<RequestResponseDto> responseList = new ArrayList<>();
        for (RequestEntity req : dbRequests) {
            // Apply category filter if provided (Advanced search/filter)
            if (category != null && !category.trim().isEmpty() && !req.getCategory().equalsIgnoreCase(category)) {
                continue;
            }
            if (!req.getDeleted()) {
                responseList.add(new RequestResponseDto(req));
            }
        }

        return ResponseEntity.ok(responseList);
    }

    @PostMapping
    public ResponseEntity<?> createRequest(@RequestBody RequestCreateDto reqData) {
        CustomPrincipal principal = (CustomPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        String newId = "req-" + System.currentTimeMillis();

        RequestEntity newRequest = new RequestEntity();
        newRequest.setId(newId);
        newRequest.setUserId(principal.getId());
        newRequest.setUserName(reqData.getUserName());
        newRequest.setUserPhone(reqData.getUserPhone());

        if (reqData.getVehicle() != null) {
            newRequest.setVehicleMake(reqData.getVehicle().getMake());
            newRequest.setVehicleModel(reqData.getVehicle().getModel());
            newRequest.setVehicleYear(reqData.getVehicle().getYear());
            newRequest.setVehiclePlate(reqData.getVehicle().getPlate());
            newRequest.setVehicleInsurance(reqData.getVehicle().getInsurance());
        }

        newRequest.setCategory(reqData.getCategory());
        newRequest.setSymptoms(reqData.getSymptoms());
        newRequest.setDescription(reqData.getDescription());
        newRequest.setLocation(reqData.getLocation());

        if (reqData.getGps() != null) {
            newRequest.setGpsLat(reqData.getGps().getLat());
            newRequest.setGpsLng(reqData.getGps().getLng());
        }

        newRequest.setImageSimulated(reqData.getImageSimulated() != null ? reqData.getImageSimulated() : false);
        newRequest.setAudioSimulated(reqData.getAudioSimulated() != null ? reqData.getAudioSimulated() : false);
        newRequest.setIsTowingRequest(reqData.getIsTowingRequest() != null ? reqData.getIsTowingRequest() : false);
        newRequest.setDestination(reqData.getDestination());

        if (reqData.getDestinationGps() != null) {
            newRequest.setDestinationGpsLat(reqData.getDestinationGps().getLat());
            newRequest.setDestinationGpsLng(reqData.getDestinationGps().getLng());
        }

        newRequest.setEta("Searching for helpers...");
        newRequest.setStatus("pending");
        newRequest.setPaymentStatus("unpaid");

        requestRepository.save(newRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(new RequestResponseDto(newRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRequest(@PathVariable("id") String id, @RequestBody RequestUpdateDto reqData) {
        Optional<RequestEntity> reqOpt = requestRepository.findById(id);

        if (reqOpt.isEmpty() || reqOpt.get().getDeleted()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Request not found."));
        }

        RequestEntity request = reqOpt.get();
        String status = reqData.getStatus();

        String defaultEta = request.getEta();
        if ("accepted".equals(status)) defaultEta = "Assigning tech...";
        else if ("technician_assigned".equals(status)) defaultEta = "25 mins";
        else if ("on_the_way".equals(status)) defaultEta = "12 mins";
        else if ("repair_in_progress".equals(status)) defaultEta = "Under repair";
        else if ("completed".equals(status)) defaultEta = "Completed";

        if (reqData.getAdditionalFields() != null) {
            RequestUpdateDto.AdditionalFieldsDto fields = reqData.getAdditionalFields();
            if (fields.getEta() != null) {
                defaultEta = fields.getEta();
            }

            if (fields.getGarageId() != null) request.setGarageId(fields.getGarageId());
            if (fields.getGarageName() != null) request.setGarageName(fields.getGarageName());
            if (fields.getTowingId() != null) request.setTowingId(fields.getTowingId());
            if (fields.getTowingName() != null) request.setTowingName(fields.getTowingName());

            if (fields.getTechnician() != null) {
                request.setTechnicianId(fields.getTechnician().getId());
                request.setTechnicianName(fields.getTechnician().getName());
                request.setTechnicianPhone(fields.getTechnician().getPhone());
            }
        }

        request.setStatus(status);
        request.setEta(defaultEta);
        request.setUpdatedAt(Instant.now().toString());

        // Calculations on Accept
        if ("accepted".equals(status)) {
            String providerId = request.getGarageId() != null ? request.getGarageId() : request.getTowingId();
            if (providerId != null) {
                Optional<User> providerOpt = userRepository.findById(providerId);
                if (providerOpt.isPresent()) {
                    User provider = providerOpt.get();
                    Double ratePerKM = provider.getRatePerKM() != null ? provider.getRatePerKM() : ("towing".equals(provider.getRole()) ? 200.0 : 150.0);
                    
                    double distance = calculateHaversineDistance(request.getGpsLat(), request.getGpsLng(), provider.getGpsLat(), provider.getGpsLng());
                    
                    double PLATFORM_COMMISSION_RATE = 0.1;
                    double dispatchCost = Math.round(((distance * ratePerKM) / 300.0) * 100.0) / 100.0;
                    double platformCommission = Math.round(dispatchCost * PLATFORM_COMMISSION_RATE * 100.0) / 100.0;
                    double providerShare = Math.round((dispatchCost - platformCommission) * 100.0) / 100.0;

                    request.setDistance(distance);
                    request.setDispatchCost(dispatchCost);
                    request.setPlatformCommission(platformCommission);
                    request.setProviderShare(providerShare);
                    request.setFee(String.format("$%.2f", dispatchCost));
                }
            }
        }
        // Reset cost on Cancel
        if ("cancelled".equalsIgnoreCase(status) || 
            ("completed".equalsIgnoreCase(status) && "Cancelled".equalsIgnoreCase(defaultEta))) {
            request.setFee("$0.00");
            request.setDispatchCost(0.0);
            request.setPlatformCommission(0.0);
            request.setProviderShare(0.0);
            request.setDistance(0.0);
        }

        requestRepository.save(request);

        // Create notification on Accept
        if ("accepted".equals(status) && request.getUserId() != null) {
            String notifType = request.getIsTowingRequest() ? "tow_accepted" : "accepted";
            
            // Check if already notified
            // In Spring Data JPA, we can query it or simply create it. Since it's a new status change, creating it is appropriate.
            Notification notif = new Notification();
            notif.setId("notif-" + System.currentTimeMillis());
            notif.setUserId(request.getUserId());
            notif.setType(notifType);
            notif.setRequestId(request.getId());
            
            if (request.getIsTowingRequest()) {
                notif.setTitle("Tow Truck Accepted Your Request");
                notif.setMessage((request.getTowingName() != null ? request.getTowingName() : "A tow truck driver") + 
                                 " has accepted your " + request.getCategory() + " tow request and is heading to your location.");
            } else {
                notif.setTitle("Garage Accepted Your Request");
                notif.setMessage((request.getGarageName() != null ? request.getGarageName() : "A nearby garage") + 
                                 " has accepted your " + request.getCategory() + " request and is preparing assistance.");
            }
            notificationRepository.save(notif);
        }

        return ResponseEntity.ok(new RequestResponseDto(request));
    }

    // CSV Export - Beyond CRUD feature!
    @GetMapping("/export")
    public void exportRequestsToCSV(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; file=requests_report.csv");

        List<RequestEntity> requests = requestRepository.findAllByOrderByCreatedAtDesc();

        PrintWriter writer = response.getWriter();
        // Write header
        writer.println("Request ID,User Name,Category,Location,Status,Payment Status,Garage Name,Towing Name,Fee,Timestamp");

        // Write rows
        for (RequestEntity req : requests) {
            if (!req.getDeleted()) {
                writer.println(String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s",
                        escapeCsv(req.getId()),
                        escapeCsv(req.getUserName()),
                        escapeCsv(req.getCategory()),
                        escapeCsv(req.getLocation()),
                        escapeCsv(req.getStatus()),
                        escapeCsv(req.getPaymentStatus()),
                        escapeCsv(req.getGarageName() != null ? req.getGarageName() : ""),
                        escapeCsv(req.getTowingName() != null ? req.getTowingName() : ""),
                        escapeCsv(req.getFee() != null ? req.getFee() : ""),
                        escapeCsv(req.getCreatedAt())
                ));
            }
        }
        writer.flush();
        writer.close();
    }

    private String escapeCsv(String val) {
        if (val == null) return "";
        if (val.contains(",") || val.contains("\"") || val.contains("\n")) {
            return "\"" + val.replace("\"", "\"\"") + "\"";
        }
        return val;
    }
}
