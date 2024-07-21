package net.javaguides.springboot.controller;

import net.javaguides.springboot.model.User;
import net.javaguides.springboot.service.AccessService;
import net.javaguides.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/roles")
@CrossOrigin
public class RoleController {

    private final UserService userService;
    private final AccessService accessService;

    @Autowired
    public RoleController(UserService userService, AccessService accessService) {
        this.userService = userService;
        this.accessService = accessService;
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'CONTROL_ADMIN')")
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'CONTROL_ADMIN')")
    @PutMapping("/users/{id}/role")
    public ResponseEntity<?> updateUserRole(@PathVariable Long id, @RequestParam String role) {
        userService.updateUserRole(id, role);
        return ResponseEntity.ok("User role updated successfully");
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'CONTROL_ADMIN')")
    @GetMapping("/users/{id}/access")
    public ResponseEntity<?> getUserAccess(@PathVariable Long id) {
        Map<String, Boolean> accessMap = accessService.getUserAccess(id);
        return ResponseEntity.ok(accessMap);
    }
}
