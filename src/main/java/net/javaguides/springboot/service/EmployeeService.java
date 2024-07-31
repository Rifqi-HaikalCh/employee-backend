package net.javaguides.springboot.service;

import net.javaguides.springboot.model.Employee;
import net.javaguides.springboot.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public boolean isEmailUnique(String emailId) {
        // Ensure that findByEmailId returns an Employee or null
        return employeeRepository.findByEmailId(emailId) == null;
    }

    public Employee createEmployee(Employee employee) {
        if (!isEmailUnique(employee.getEmailId())) {
            throw new IllegalArgumentException("Email address already exists");
        }
        return employeeRepository.save(employee);
    }

    public Employee updateEmployee(String id, Employee employee) {
        Optional<Employee> existingEmployeeOptional = employeeRepository.findById(Long.parseLong(id));
        if (existingEmployeeOptional.isPresent()) {
            Employee existingEmployee = existingEmployeeOptional.get();
            existingEmployee.setFirstName(employee.getFirstName());
            existingEmployee.setLastName(employee.getLastName());
            existingEmployee.setEmailId(employee.getEmailId());
            existingEmployee.setDateOfBirth(employee.getDateOfBirth());
            return employeeRepository.save(existingEmployee);
        } else {
            throw new IllegalArgumentException("Employee not found with id: " + id);
        }
    }

    public void deleteEmployee(String id) {
        if (employeeRepository.existsById(Long.parseLong(id))) {
            employeeRepository.deleteById(Long.parseLong(id));
        } else {
            throw new IllegalArgumentException("Employee not found with id: " + id);
        }
    }
}

