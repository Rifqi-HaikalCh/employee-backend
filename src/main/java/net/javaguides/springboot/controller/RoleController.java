package net.javaguides.springboot.controller;

// RoleController.java
import net.javaguides.springboot.dto.UserRoleDto;
import net.javaguides.springboot.model.User;
import net.javaguides.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
public class RoleController {
    private final UserService userService;

    @Autowired
    public RoleController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<UserRoleDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PutMapping("/users/{userId}/role")
    public ResponseEntity<?> updateUserRole(@PathVariable Long userId, @RequestParam String roleName) {
        try {
            userService.updateUserRole(userId, roleName);
            return ResponseEntity.ok("User role updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating user role: " + e.getMessage());
        }
    }
}


