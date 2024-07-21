package net.javaguides.springboot.controller;

import net.javaguides.springboot.dto.UserDto;
import net.javaguides.springboot.model.RoleEntity;
import net.javaguides.springboot.model.User;
import net.javaguides.springboot.security.JwtTokenUtil;
import net.javaguides.springboot.service.AccessService;
import net.javaguides.springboot.service.JwtUserDetailsService;
import net.javaguides.springboot.service.RoleService;
import net.javaguides.springboot.model.JwtResponse;
import net.javaguides.springboot.model.JwtRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping("/auth")
@RestController
@CrossOrigin
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final JwtUserDetailsService userDetailsService;
    private final RoleService roleService;
    private final AccessService accessService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil,
                          JwtUserDetailsService userDetailsService, RoleService roleService, AccessService accessService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
        this.roleService = roleService;
        this.accessService = accessService;
    }

    // In AuthController
    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) {
        if (authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword())) {
            final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
            final String token = jwtTokenUtil.generateToken(authenticationRequest.getUsername());
            User user = userDetailsService.findByUsername(authenticationRequest.getUsername()).get();

            Map<String, Boolean> accessMap = accessService.getUserAccess(user.getId());

            // Ensure email is included in the response
            return ResponseEntity.ok(new JwtResponse(token, true, userDetails.getAuthorities().toString(), user.getEmail(), accessMap));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new JwtResponse("", false, "", null, null));
        }
    }


    @PostMapping("/register")
    public ResponseEntity<?> saveUser(@RequestBody UserDto userDto) {
        if (userDetailsService.usernameExists(userDto.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
        }

        if (userDetailsService.emailExists(userDto.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists");
        }

        if (!userDto.getPassword().equals(userDto.getConfirmation())) {
            return ResponseEntity.badRequest().body("Password and confirmation do not match");
        }

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setConfirmation(userDto.getConfirmation());

        RoleEntity role = roleService.findByName("USER");
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
