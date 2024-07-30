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

import static net.javaguides.springboot.security.JwtRequestFilter.logger;

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

    public List<UserRoleDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapUserToUserRoleDto)
                .collect(Collectors.toList());
    }

    private UserRoleDto mapUserToUserRoleDto(User user) {
        UserRoleDto.Roles roles = new UserRoleDto.Roles();
        RoleEntity userRole = user.getRole();
        if (userRole != null) {
            roles.setUser(userRole.getName().equals("USER"));
            roles.setSuperAdmin(userRole.getName().equals("SUPER_ADMIN"));
            roles.setStaffAdmin(userRole.getName().equals("STAFF_ADMIN"));
            roles.setControlAdmin(userRole.getName().equals("CONTROL_ADMIN"));
        }

        return new UserRoleDto(
                user.getId(),
                user.getUsername(),
                roles
        );
    }

    public void updateUserRole(Long userId, String roleName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        RoleEntity role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        user.setRole(role);
        userRepository.save(user);
    }
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    public UserProfileDto getUserProfile(String username) {
        try {
            return userRepository.findByUsername(username)
                    .map(user -> {
                        if (user.getRole() == null) {
                            throw new RuntimeException("User role is null for user: " + username);
                        }
                        return new UserProfileDto(user.getUsername(), user.getEmail(), user.getRole().getDisplayName());
                    })
                    .orElseThrow(() -> new UserNotFoundException("User not found: " + username));
        } catch (UserNotFoundException e) {
            throw e;  // Re-throw UserNotFoundException to be caught in the controller
        } catch (Exception e) {
            // Log the exception
            logger.error("Error retrieving user profile for user: " + username, e);
            throw new RuntimeException("Error retrieving user profile", e);
        }
    }

    public void deleteUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        userRepository.delete(user);
    }
}
