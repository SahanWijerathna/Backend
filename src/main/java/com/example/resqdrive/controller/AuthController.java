package com.resqdrive.backend.controller;

import com.resqdrive.backend.config.JwtFilter.CustomPrincipal;
import com.resqdrive.backend.config.JwtUtil;
import com.resqdrive.backend.dto.LoginRequest;
import com.resqdrive.backend.dto.RegisterRequest;
import com.resqdrive.backend.dto.UserResponseDto;
import com.resqdrive.backend.model.*;
import com.resqdrive.backend.repository.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private TechnicianRepository technicianRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JavaMailSender mailSender;

    @Value("${resqdrive.frontend.url:http://localhost:5173}")
    private String frontendUrl;

    private void addTokenCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie("vamp-token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60); // 7 days in seconds
        // Set secure flags for cookie
        cookie.setSecure(false); // Can be set to true in production profile
        response.addCookie(cookie);
    }

    @PostMapping("/register")
    @Transactional
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest details, HttpServletResponse response) {
        // Check if user exists
        Optional<User> existing = userRepository.findByEmail(details.getEmail().toLowerCase());
        if (existing.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Email is already registered."));
        }

        String userId = details.getRole().substring(0, 3) + "-" + System.currentTimeMillis();
        String hashedPassword = passwordEncoder.encode(details.getPassword());

        User newUser = new User();
        newUser.setId(userId);
        newUser.setEmail(details.getEmail().toLowerCase());
        newUser.setPassword(hashedPassword);
        newUser.setRole(details.getRole());
        newUser.setName(details.getName());
        newUser.setPhone(details.getPhone() != null ? details.getPhone() : "+1 (555) 000-0000");

        // Role specific logic
        if ("user".equals(details.getRole())) {
            newUser.setGpsLat(details.getGps() != null ? details.getGps().getLat() : 37.7891);
            newUser.setGpsLng(details.getGps() != null ? details.getGps().getLng() : -122.4014);
        } else if ("garage".equals(details.getRole())) {
            newUser.setName(details.getGarageName() != null ? details.getGarageName() : details.getName());
            newUser.setOwnerName(details.getName());
            newUser.setAddress(details.getAddress() != null ? details.getAddress() : "100 Main St");
            newUser.setHours(details.getHours() != null ? details.getHours() : "08:00 - 18:00");
            newUser.setRatePerKM(150.0);
            newUser.setGpsLat(details.getGps() != null ? details.getGps().getLat() : 37.7891);
            newUser.setGpsLng(details.getGps() != null ? details.getGps().getLng() : -122.4014);
        } else if ("towing".equals(details.getRole())) {
            newUser.setName(details.getCompanyName() != null ? details.getCompanyName() : details.getName() + " Towing");
            newUser.setOperatorName(details.getName());
            newUser.setTruckPlate(details.getTruckPlate() != null ? details.getTruckPlate() : "TOW-MOCK");
            newUser.setRatePerKM(details.getRatePerKM() != null ? details.getRatePerKM() : 200.0);
            newUser.setGpsLat(details.getGps() != null ? details.getGps().getLat() : 37.7562);
            newUser.setGpsLng(details.getGps() != null ? details.getGps().getLng() : -122.4498);
        } else if ("admin".equals(details.getRole())) {
            // General settings
        }

        userRepository.save(newUser);

        // Save nested models if provided
        if ("user".equals(details.getRole())) {
            List<Vehicle> vehicleList = new ArrayList<>();
            if (details.getVehicles() != null && !details.getVehicles().isEmpty()) {
                for (RegisterRequest.VehicleDto v : details.getVehicles()) {
                    Vehicle vehicle = new Vehicle();
                    vehicle.setId(v.getId() != null ? v.getId() : "veh-" + System.currentTimeMillis() + "-" + new Random().nextInt(1000));
                    vehicle.setUser(newUser);
                    vehicle.setMake(v.getMake());
                    vehicle.setModel(v.getModel());
                    vehicle.setYear(v.getYear() != null ? v.getYear() : "2023");
                    vehicle.setPlate(v.getPlate());
                    vehicle.setInsurance(v.getInsurance());
                    vehicleRepository.save(vehicle);
                    vehicleList.add(vehicle);
                }
            } else {
                // Auto seed a default vehicle if none sent
                Vehicle vehicle = new Vehicle();
                vehicle.setId("veh-" + System.currentTimeMillis());
                vehicle.setUser(newUser);
                vehicle.setMake("Tesla");
                vehicle.setModel("Model 3");
                vehicle.setYear("2023");
                vehicle.setPlate("MOCK-PLT");
                vehicle.setInsurance("Mock Insurance Co.");
                vehicleRepository.save(vehicle);
                vehicleList.add(vehicle);
            }
            newUser.setVehicles(vehicleList);
        } else if ("garage".equals(details.getRole())) {
            List<ServiceEntity> serviceList = new ArrayList<>();
            // Auto seed default services if none provided
            ServiceEntity s1 = new ServiceEntity();
            s1.setId("srv-1-" + System.currentTimeMillis());
            s1.setGarage(newUser);
            s1.setName("Engine Issue");
            s1.setPrice("$150");
            s1.setDesc("Standard Diagnostic Check");
            serviceRepository.save(s1);
            serviceList.add(s1);

            ServiceEntity s2 = new ServiceEntity();
            s2.setId("srv-2-" + System.currentTimeMillis());
            s2.setGarage(newUser);
            s2.setName("Tire Issue");
            s2.setPrice("$60");
            s2.setDesc("Flat tire repairs & replacement");
            serviceRepository.save(s2);
            serviceList.add(s2);
            newUser.setServices(serviceList);

            List<Technician> techList = new ArrayList<>();
            Technician tech = new Technician();
            tech.setId("tech-1-" + System.currentTimeMillis());
            tech.setGarage(newUser);
            tech.setName("Bob Builder");
            tech.setPhone(newUser.getPhone());
            tech.setStatus("available");
            technicianRepository.save(tech);
            techList.add(tech);
            newUser.setTechnicians(techList);
        }

        String token = jwtUtil.generateToken(newUser.getEmail(), newUser.getRole(), newUser.getId());
        addTokenCookie(response, token);

        UserResponseDto responseDto = new UserResponseDto(newUser);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("success", true, "user", responseDto, "token", token));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest credentials, HttpServletResponse response) {
        Optional<User> userOpt = userRepository.findByEmailAndRole(
                credentials.getEmail().toLowerCase(), credentials.getRole());

        if (userOpt.isEmpty() || userOpt.get().getDeleted()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid email, password, or role choice."));
        }

        User user = userOpt.get();
        if (!passwordEncoder.matches(credentials.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid email, password, or role choice."));
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole(), user.getId());
        addTokenCookie(response, token);

        UserResponseDto responseDto = new UserResponseDto(user);
        return ResponseEntity.ok(Map.of("success", true, "user", responseDto, "token", token));
    }

    @GetMapping("/me")
    public ResponseEntity<?> me() {
        CustomPrincipal principal = (CustomPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> userOpt = userRepository.findById(principal.getId());

        if (userOpt.isEmpty() || userOpt.get().getDeleted()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "User not found"));
        }

        return ResponseEntity.ok(Map.of("success", true, "user", new UserResponseDto(userOpt.get())));
    }

    @PutMapping("/profile")
    @Transactional
    public ResponseEntity<?> updateProfile(@RequestBody Map<String, Object> details) {
        CustomPrincipal principal = (CustomPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> userOpt = userRepository.findById(principal.getId());

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "User not found"));
        }

        User user = userOpt.get();

        if (details.containsKey("name")) user.setName((String) details.get("name"));
        if (details.containsKey("phone")) user.setPhone((String) details.get("phone"));
        if (details.containsKey("ownerName")) user.setOwnerName((String) details.get("ownerName"));
        if (details.containsKey("address")) user.setAddress((String) details.get("address"));
        if (details.containsKey("hours")) user.setHours((String) details.get("hours"));
        
        if (details.containsKey("coverageRadius")) {
            user.setCoverageRadius(String.valueOf(details.get("coverageRadius")));
        }
        
        if (details.containsKey("ratePerKM")) {
            user.setRatePerKM(Double.valueOf(String.valueOf(details.get("ratePerKM"))));
        }
        
        if (details.containsKey("operatorName")) user.setOperatorName((String) details.get("operatorName"));
        if (details.containsKey("truckPlate")) user.setTruckPlate((String) details.get("truckPlate"));

        if (details.containsKey("gps")) {
            Map<?, ?> gpsMap = (Map<?, ?>) details.get("gps");
            if (gpsMap != null) {
                user.setGpsLat(Double.valueOf(String.valueOf(gpsMap.get("lat"))));
                user.setGpsLng(Double.valueOf(String.valueOf(gpsMap.get("lng"))));
            }
        }

        // Handle nested replacement for user vehicles
        if ("user".equals(user.getRole()) && details.containsKey("vehicles")) {
            user.getVehicles().clear();
            List<?> vehicleListRaw = (List<?>) details.get("vehicles");
            if (vehicleListRaw != null) {
                for (Object vObj : vehicleListRaw) {
                    Map<?, ?> vMap = (Map<?, ?>) vObj;
                    Vehicle vehicle = new Vehicle();
                    vehicle.setId(vMap.get("id") != null ? (String) vMap.get("id") : "veh-" + System.currentTimeMillis() + "-" + new Random().nextInt(100));
                    vehicle.setUser(user);
                    vehicle.setMake((String) vMap.get("make"));
                    vehicle.setModel((String) vMap.get("model"));
                    vehicle.setYear(vMap.get("year") != null ? (String) vMap.get("year") : "2023");
                    vehicle.setPlate((String) vMap.get("plate"));
                    vehicle.setInsurance((String) vMap.get("insurance"));
                    user.getVehicles().add(vehicle);
                }
            }
        }

        // Handle nested services/technicians for garages
        if ("garage".equals(user.getRole())) {
            if (details.containsKey("services")) {
                user.getServices().clear();
                List<?> serviceListRaw = (List<?>) details.get("services");
                if (serviceListRaw != null) {
                    for (Object sObj : serviceListRaw) {
                        Map<?, ?> sMap = (Map<?, ?>) sObj;
                        ServiceEntity service = new ServiceEntity();
                        service.setId(sMap.get("id") != null ? (String) sMap.get("id") : "srv-" + System.currentTimeMillis() + "-" + new Random().nextInt(100));
                        service.setGarage(user);
                        service.setName((String) sMap.get("name"));
                        service.setPrice((String) sMap.get("price"));
                        service.setDesc((String) sMap.get("desc"));
                        user.getServices().add(service);
                    }
                }
            }

            if (details.containsKey("technicians")) {
                user.getTechnicians().clear();
                List<?> techListRaw = (List<?>) details.get("technicians");
                if (techListRaw != null) {
                    for (Object tObj : techListRaw) {
                        Map<?, ?> tMap = (Map<?, ?>) tObj;
                        Technician tech = new Technician();
                        tech.setId(tMap.get("id") != null ? (String) tMap.get("id") : "tech-" + System.currentTimeMillis() + "-" + new Random().nextInt(100));
                        tech.setGarage(user);
                        tech.setName((String) tMap.get("name"));
                        tech.setPhone((String) tMap.get("phone"));
                        tech.setStatus(tMap.get("status") != null ? (String) tMap.get("status") : "available");
                        user.getTechnicians().add(tech);
                    }
                }
            }
        }

        userRepository.save(user);

        return ResponseEntity.ok(Map.of("success", true, "user", new UserResponseDto(user)));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        if (email == null || email.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Email is required."));
        }

        Optional<User> userOpt = userRepository.findByEmail(email.toLowerCase().trim());
        if (userOpt.isEmpty() || userOpt.get().getDeleted()) {
            return ResponseEntity.ok(Map.of("success", true, "message", "If an account matches that email, we have dispatched instructions for securely resetting your password."));
        }

        User user = userOpt.get();
        String token = jwtUtil.generateResetToken(user.getEmail());
        String resetLink = frontendUrl + "/reset-password?token=" + token;

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("no-reply@resqdrive.com");
            message.setTo(user.getEmail());
            message.setSubject("Resqdrive - Password Recovery");
            message.setText("Hello " + user.getName() + ",\n\n" +
                    "We received a request to reset your password. Click the link below to securely set a new password:\n" +
                    resetLink + "\n\n" +
                    "This link will expire in 15 minutes.\n\n" +
                    "Best regards,\nResqdrive Team");
            mailSender.send(message);
        } catch (Exception e) {
            System.out.println("=================================================");
            System.out.println("PASSWORD RESET LINK (SMTP failed/not configured):");
            System.out.println(resetLink);
            System.out.println("=================================================");
        }

        return ResponseEntity.ok(Map.of("success", true, "message", "If an account matches that email, we have dispatched instructions for securely resetting your password."));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> body) {
        String token = body.get("token");
        String newPassword = body.get("password");

        if (token == null || token.trim().isEmpty() || newPassword == null || newPassword.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Token and password are required."));
        }

        if (!jwtUtil.validateTokenOnly(token)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Reset link has expired or is invalid."));
        }

        String email = jwtUtil.extractUsername(token);
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty() || userOpt.get().getDeleted()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "User not found."));
        }

        User user = userOpt.get();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return ResponseEntity.ok(Map.of("success", true, "message", "Password reset successfully. You can now login with your new password."));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("vamp-token", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0); // Expires immediately
        response.addCookie(cookie);
        return ResponseEntity.ok(Map.of("success", true, "message", "Successfully logged out."));
    }
}
