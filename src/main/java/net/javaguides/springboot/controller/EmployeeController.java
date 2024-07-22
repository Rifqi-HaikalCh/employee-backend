package net.javaguides.springboot.controller;

import net.javaguides.springboot.model.Employee;
import net.javaguides.springboot.repository.UserRepository;
import net.javaguides.springboot.service.AccessService;
import net.javaguides.springboot.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final AccessService accessService;
    private final UserRepository userRepository;

    @Autowired
    public EmployeeController(EmployeeService employeeService, AccessService accessService, UserRepository userRepository) {
        this.employeeService = employeeService;
        this.accessService = accessService;
        this.userRepository = userRepository;
    }

    @GetMapping("/data")
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }


    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'STAFF_ADMIN')")
    @PostMapping("/cont/add")
    public Employee createEmployee(@RequestBody Employee employee, Principal principal) {
        Long userId = getUserId(principal.getName());
        checkAccess(userId, "employeeList");
        return employeeService.createEmployee(employee);
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'STAFF_ADMIN')")
    @PutMapping("/cont/{id}")
    public Employee updateEmployee(@PathVariable Long id, @RequestBody Employee employee, Principal principal) {
        Long userId = getUserId(principal.getName());
        checkAccess(userId, "employeeList");
        return employeeService.updateEmployee(id, employee);
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'STAFF_ADMIN')")
    @DeleteMapping("/cont/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable Long id, Principal principal) {
        Long userId = getUserId(principal.getName());
        checkAccess(userId, "employeeList");
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok("Employee deleted successfully");
    }

    private Long getUserId(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username))
                .getId();
    }

    private void checkAccess(Long userId, String resource) {
        Map<String, Boolean> accessMap = accessService.getUserAccess(userId);
        if (!Boolean.TRUE.equals(accessMap.get(resource))) {
            throw new AccessDeniedException("Access denied for resource: " + resource);
        }
    }
}
