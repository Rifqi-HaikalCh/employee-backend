package net.javaguides.springboot.dto;

import lombok.Data;

@Data
public class UserDto {
    private String username;
    private String email;
    private String password;
    private String confirmPassword;
    private String role; // Tambahkan field role
}
