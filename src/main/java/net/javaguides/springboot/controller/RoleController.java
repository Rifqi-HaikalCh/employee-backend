package net.javaguides.springboot.controller;

import net.javaguides.springboot.dto.UserRoleDto;
import net.javaguides.springboot.dto.UserRoleUpdateDto;
import net.javaguides.springboot.model.User;
import net.javaguides.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @PutMapping("/users/roles/{id}")
    public ResponseEntity<Map<String, String>> updateUserRole(@PathVariable Long id, @RequestBody UserRoleUpdateDto roleUpdateDto) {
        try {
            userService.updateUserRole(id, roleUpdateDto);
            Map<String, String> response = new HashMap<>();
            response.put("message", "User role updated successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Error updating user role: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }



}


