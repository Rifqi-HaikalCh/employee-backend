package net.javaguides.springboot.controller;

import net.javaguides.springboot.dto.UserProfileDto;
import net.javaguides.springboot.service.AccessService;
import net.javaguides.springboot.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/access")
public class AccessController {

    private final AccessService accessService;
    private final UserService userService;

    public AccessController(AccessService accessService, UserService userService) {
        this.accessService = accessService;
        this.userService = userService;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/access/{userId}")
    public ResponseEntity<Map<String, Boolean>> getUserAccess(@PathVariable Long userId) {
        try {
            Map<String, Boolean> accessMap = accessService.getUserAccess(userId);
            return ResponseEntity.ok(accessMap);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/profile/{username}")
    public ResponseEntity<UserProfileDto> getUserProfile(@PathVariable String username) {
        UserProfileDto userProfile = userService.getUserProfile(username);
        return ResponseEntity.ok(userProfile);
    }
}

