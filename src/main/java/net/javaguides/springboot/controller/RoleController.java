package net.javaguides.springboot.controller;

import net.javaguides.springboot.model.User;
import net.javaguides.springboot.dto.UserRoleDto;
import net.javaguides.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/roles")
public class RoleController {

    private final UserService userService;

    @Autowired
    public RoleController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserRoleDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

//    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'CONTROL_ADMIN')")
    @PutMapping("/users/{id}/role")
    public ResponseEntity<?> updateUserRole(@PathVariable Long id, @RequestParam Long roleId) {
        try {
            userService.updateUserRole(id, roleId);
            return ResponseEntity.ok("User role updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating user role: " + e.getMessage());
        }
    }
}
