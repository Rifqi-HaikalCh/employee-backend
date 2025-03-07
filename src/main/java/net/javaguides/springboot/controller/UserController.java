package net.javaguides.springboot.controller;

import net.javaguides.springboot.dto.UserProfileDto;
import net.javaguides.springboot.exception.UserNotFoundException; // Import the custom exception
import net.javaguides.springboot.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(Principal principal) {
        String username = principal.getName();

        try {
            UserProfileDto userProfile = userService.getUserProfile(username);
            return ResponseEntity.ok(userProfile);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            // Log the exception
            logger.error("Error retrieving user profile for user: " + username, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving user profile");
        }
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/profile")
    public ResponseEntity<?> deleteProfile(Principal principal) {
        try {
            userService.deleteUser(principal.getName());
            return ResponseEntity.ok("User deleted successfully");
        } catch (Exception e) {
            logger.error("Failed to delete user: " + principal.getName(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete user: " + e.getMessage());
        }
    }
}
