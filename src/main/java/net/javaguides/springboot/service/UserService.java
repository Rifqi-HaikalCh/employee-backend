package net.javaguides.springboot.service;

import net.javaguides.springboot.dto.UserDto;
import net.javaguides.springboot.dto.UserProfileDto;
import net.javaguides.springboot.dto.UserRoleDto;
import net.javaguides.springboot.dto.UserRoleUpdateDto;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static net.javaguides.springboot.security.JwtRequestFilter.logger;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final AccessService accessService;

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
        if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        if (!userDto.getPassword().equals(userDto.getConfirmation())) {
            throw new RuntimeException("Passwords do not match");
        }

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

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

        return new JwtResponse(token, true, user.getRole().getDisplayName(), user.getEmail(), accessMap);
    }

    public List<UserRoleDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapUserToUserRoleDto)
                .collect(Collectors.toList());
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    @Transactional
    public void updateUserRole(Long userId, UserRoleUpdateDto roleUpdateDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        RoleEntity newRole = roleRepository.findByName(roleUpdateDto.getRoleName())
                .orElseThrow(() -> new RuntimeException("Role not found with name: " + roleUpdateDto.getRoleName()));

        user.setRole(newRole);
        userRepository.save(user);
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
            throw e;
        } catch (Exception e) {
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
