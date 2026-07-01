package com.resqdrive.backend.controller;

import com.resqdrive.backend.config.JwtFilter.CustomPrincipal;
import com.resqdrive.backend.dto.UserResponseDto;
import com.resqdrive.backend.model.User;
import com.resqdrive.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<?> getUsers(@RequestParam(value = "role", required = false) String role) {
        List<User> users;
        if (role != null && !role.trim().isEmpty()) {
            users = userRepository.findByRole(role);
        } else {
            users = userRepository.findAll();
        }

        List<UserResponseDto> responseList = new ArrayList<>();
        for (User user : users) {
            if (!user.getDeleted()) {
                responseList.add(new UserResponseDto(user));
            }
        }
        return ResponseEntity.ok(responseList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUserStatus(@PathVariable("id") String id, @RequestBody Map<String, Object> body) {
        CustomPrincipal principal = (CustomPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        // Only admin is allowed to modify another user's status
        if (!"admin".equals(principal.getRole())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "Access forbidden. Admin only."));
        }

        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty() || userOpt.get().getDeleted()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "User not found"));
        }

        User user = userOpt.get();
        if (body.containsKey("suspended")) {
            user.setSuspended((Boolean) body.get("suspended"));
        }

        userRepository.save(user);
        return ResponseEntity.ok(Map.of("success", true, "user", new UserResponseDto(user)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") String id) {
        CustomPrincipal principal = (CustomPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Self-delete or admin delete
        if (!principal.getId().equals(id) && !"admin".equals(principal.getRole())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "Access forbidden."));
        }

        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty() || userOpt.get().getDeleted()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "User not found"));
        }

        User user = userOpt.get();
        // Implement Soft Delete
        user.setDeleted(true);
        userRepository.save(user);

        return ResponseEntity.ok(Map.of("success", true, "message", "Account deleted successfully."));
    }
}
