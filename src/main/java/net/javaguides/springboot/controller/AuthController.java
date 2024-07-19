package net.javaguides.springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

// Your custom classes
import net.javaguides.springboot.security.JwtTokenUtil;
import net.javaguides.springboot.service.JwtUserDetailsService;
import net.javaguides.springboot.model.JwtRequest;
import net.javaguides.springboot.model.JwtResponse;
import net.javaguides.springboot.dto.UserDto;
import net.javaguides.springboot.model.User;

@RestController
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final JwtUserDetailsService userDetailsService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, JwtUserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) {
        if (authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword())) {
            // Load user details and generate JWT token
            final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
            final String token = jwtTokenUtil.generateToken(userDetails);

            // Get roles of the user
            String roles = userDetails.getAuthorities().toString();

            // Return JWT response with token and roles
            JwtResponse response = new JwtResponse(token, true, roles);
            return ResponseEntity.ok(response);
        } else {
            // Return response for unauthorized access
            JwtResponse response = new JwtResponse("", false, "");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> saveUser(@RequestBody UserDto user) {
        // Check if username already exists
        if (userDetailsService.usernameExists(user.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
        }

        // Validate password and confirmation
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            return ResponseEntity.badRequest().body("Password and confirmation do not match");
        }

        // Set role for new user (in this case, role "USER")
        user.setRole("USER");

        // Save user and return response
        User savedUser = userDetailsService.save(user);
        return ResponseEntity.ok(savedUser);
    }

    private boolean authenticate(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
