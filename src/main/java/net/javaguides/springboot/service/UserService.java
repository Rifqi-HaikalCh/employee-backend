package net.javaguides.springboot.service;

import net.javaguides.springboot.dto.UserDto;
import net.javaguides.springboot.dto.UserProfileDto;
import net.javaguides.springboot.dto.UserRoleDto;
import net.javaguides.springboot.exception.UserNotFoundException;
import net.javaguides.springboot.model.JwtResponse;
import net.javaguides.springboot.model.RoleEntity;
import net.javaguides.springboot.model.User;
import net.javaguides.springboot.repository.RoleRepository;
import net.javaguides.springboot.repository.UserRepository;
import net.javaguides.springboot.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil; // Add JwtTokenUtil for token generation
    private final AccessService accessService; // Add AccessService for access map

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder, JwtTokenUtil jwtTokenUtil,
                       AccessService accessService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtil = jwtTokenUtil;
        this.accessService = accessService;
    }

    public User registerUser(UserDto userDto) {
        // Validate if user already exists
        if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        // Validate password confirmation
        if (!userDto.getPassword().equals(userDto.getConfirmation())) {
            throw new RuntimeException("Passwords do not match");
        }

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setConfirmation(passwordEncoder.encode(userDto.getConfirmation()));

        // Set default role for new registrations
        RoleEntity defaultRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Default role not found"));
        user.setRole(defaultRole);

        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
    }

    public JwtResponse loginUser(UserDto userDto) {
        User user = userRepository.findByUsername(userDto.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        if (!passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        final String token = jwtTokenUtil.generateToken(userDto.getUsername());

        Map<String, Boolean> accessMap = accessService.getUserAccess(user.getId());

        // Return the JwtResponse similar to the controller
        return new JwtResponse(token, true, user.getRole().getDisplayName(), user.getEmail(), accessMap);
    }

    public List<UserRoleDto> getAllUserRoles() {
        return userRepository.findAll().stream()
                .map(user -> new UserRoleDto(user.getId(), user.getUsername(), user.getRole().getDisplayName()))
                .collect(Collectors.toList());
    }

    public void updateUserRole(Long id, Long roleId) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        RoleEntity role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        user.setRole(role);
        userRepository.save(user);
    }

    public UserProfileDto getUserProfile(String username) {
        return userRepository.findByUsername(username)
                .map(user -> new UserProfileDto(user.getUsername(), user.getEmail(), user.getRole().getDisplayName()))
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public void deleteUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        userRepository.delete(user);
    }
}
