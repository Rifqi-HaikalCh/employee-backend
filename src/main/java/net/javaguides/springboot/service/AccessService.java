package net.javaguides.springboot.service;

import net.javaguides.springboot.model.RoleEntity;
import net.javaguides.springboot.model.User;
import net.javaguides.springboot.repository.RoleRepository;
import net.javaguides.springboot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class AccessService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Autowired
    public AccessService(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    public Map<String, Boolean> getUserAccess(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
        RoleEntity role = user.getRole();

        Map<String, Boolean> accessMap = new HashMap<>();
        // Default pages accessible by all roles
        accessMap.put("dashboard", true);
        accessMap.put("profile", true);

        // Determine access based on role
        switch (role.getName().toLowerCase()) {
            case "super_admin":
                accessMap.put("employeeList", true);
                accessMap.put("roleMenu", true);
                break;
            case "staff_admin":
                accessMap.put("employeeList", true);
                accessMap.put("roleMenu", false);
                break;
            case "control_admin":
                accessMap.put("employeeList", false);
                accessMap.put("roleMenu", true);
                break;
            case "user":
                accessMap.put("employeeList", false);
                accessMap.put("roleMenu", false);
                break;
            default:
                throw new IllegalArgumentException("Unknown role: " + role.getName());
        }

        return accessMap;
    }
}
