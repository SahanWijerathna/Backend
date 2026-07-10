package com.example.resqdrive.service;

import com.example.resqdrive.model.*;
import com.example.resqdrive.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DatabaseSeeder implements ApplicationRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private TechnicianRepository technicianRepository;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (requestRepository.existsById("req-default")) {
            requestRepository.deleteById("req-default");
            System.out.println("Cleaned up old default request (req-default) from database.");
        }

        if (userRepository.count() == 0) {
            System.out.println("Seeding database with default test accounts...");

            String hashedPassword = passwordEncoder.encode("password");

            // 1. Create Users
            User user1 = new User();
            user1.setId("usr-1");
            user1.setEmail("user@test.com");
            user1.setPassword(hashedPassword);
            user1.setRole("user");
            user1.setName("Alex Mercer");
            user1.setPhone("+1 (555) 019-2834");
            userRepository.save(user1);

            User user2 = new User();
            user2.setId("usr-2");
            user2.setEmail("sarah@test.com");
            user2.setPassword(hashedPassword);
            user2.setRole("user");
            user2.setName("Sarah Connor");
            user2.setPhone("+1 (555) 012-7744");
            userRepository.save(user2);

            User garage = new User();
            garage.setId("grg-1");
            garage.setEmail("garage@test.com");
            garage.setPassword(hashedPassword);
            garage.setRole("garage");
            garage.setName("Apex Auto Care");
            garage.setOwnerName("Marcus Vance");
            garage.setPhone("+1 (555) 018-9900");
            garage.setAddress("1028 Industrial Blvd, Sector 7");
            garage.setHours("08:00 - 20:00");
            garage.setCoverageRadius("15 miles");
            garage.setRatePerKM(150.0);
            garage.setGpsLat(37.7891);
            garage.setGpsLng(-122.4014);
            userRepository.save(garage);

            User towing = new User();
            towing.setId("tow-1");
            towing.setEmail("towing@test.com");
            towing.setPassword(hashedPassword);
            towing.setRole("towing");
            towing.setName("Rapid Towing & Recovery");
            towing.setOperatorName("Roy Jenkins");
            towing.setPhone("+1 (555) 017-4488");
            towing.setTruckPlate("TOW-FAST1");
            towing.setRatePerKM(200.0);
            towing.setGpsLat(37.7562);
            towing.setGpsLng(-122.4498);
            userRepository.save(towing);

            User admin = new User();
            admin.setId("adm-1");
            admin.setEmail("admin@test.com");
            admin.setPassword(hashedPassword);
            admin.setRole("admin");
            admin.setName("Super Admin");
            admin.setPhone("+1 (555) 011-0000");
            userRepository.save(admin);

            // 2. Create Vehicles for user1
            Vehicle v1 = new Vehicle();
            v1.setId("veh-1");
            v1.setUser(user1);
            v1.setMake("Tesla");
            v1.setModel("Model S");
            v1.setYear("2022");
            v1.setPlate("E-DRIVE1");
            v1.setInsurance("State Farm - SF-982312");
            vehicleRepository.save(v1);

            Vehicle v2 = new Vehicle();
            v2.setId("veh-2");
            v2.setUser(user1);
            v2.setMake("Toyota");
            v2.setModel("RAV4");
            v2.setYear("2019");
            v2.setPlate("TR-8923A");
            v2.setInsurance("Geico - GC-10293");
            vehicleRepository.save(v2);

            // 3. Create Services for garage
            ServiceEntity s1 = new ServiceEntity();
            s1.setId("srv-1");
            s1.setGarage(garage);
            s1.setName("Engine Diagnostics");
            s1.setPrice("$150");
            s1.setDesc("Full scanner sweep, cylinder compression check, and error code analysis.");
            serviceRepository.save(s1);

            ServiceEntity s2 = new ServiceEntity();
            s2.setId("srv-2");
            s2.setGarage(garage);
            s2.setName("Brake Pad Replacement");
            s2.setPrice("$120");
            s2.setDesc("Front or rear brake pads installation with rotor resurfacing check.");
            serviceRepository.save(s2);

            ServiceEntity s3 = new ServiceEntity();
            s3.setId("srv-3");
            s3.setGarage(garage);
            s3.setName("Flat Tire Patch");
            s3.setPrice("$45");
            s3.setDesc("Patch puncture from inside, balance, and adjust tire pressure.");
            serviceRepository.save(s3);

            // 4. Create Technicians for garage
            Technician t1 = new Technician();
            t1.setId("tech-1");
            t1.setGarage(garage);
            t1.setName("Marcus Vance (Owner)");
            t1.setPhone("+1 (555) 018-9900");
            t1.setStatus("available");
            technicianRepository.save(t1);

            Technician t2 = new Technician();
            t2.setId("tech-2");
            t2.setGarage(garage);
            t2.setName("Jimmy Cole");
            t2.setPhone("+1 (555) 018-9902");
            t2.setStatus("available");
            technicianRepository.save(t2);

            System.out.println("Database seeding completed successfully!");
        }
    }
}