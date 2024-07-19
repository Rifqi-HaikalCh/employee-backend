package net.javaguides.springboot.controller;

import net.javaguides.springboot.dto.UserDto;
import net.javaguides.springboot.model.JwtRequest;
import net.javaguides.springboot.model.JwtResponse;
import net.javaguides.springboot.model.RoleEntity;
import net.javaguides.springboot.model.User;
import net.javaguides.springboot.security.JwtTokenUtil;
import net.javaguides.springboot.service.JwtUserDetailsService;
import net.javaguides.springboot.service.RoleService; // Assuming you have a RoleService for managing roles
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final JwtUserDetailsService userDetailsService;
    private final RoleService roleService; // Service to handle roles

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil,
                          JwtUserDetailsService userDetailsService, RoleService roleService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
        this.roleService = roleService;
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
    public ResponseEntity<?> saveUser(@RequestBody UserDto userDto) {
        // Check if username already exists
        if (userDetailsService.usernameExists(userDto.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
        }

        // Validate password and confirmation
        if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
            return ResponseEntity.badRequest().body("Password and confirmation do not match");
        }

        // Set role for new user
        RoleEntity role = roleService.findByName(userDto.getRole());
        if (role == null) {
            // Handle case where role does not exist (optional: create new role or return error)
            return ResponseEntity.badRequest().body("Role does not exist");
        }

        // Create and save new user
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setRole(role);

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
