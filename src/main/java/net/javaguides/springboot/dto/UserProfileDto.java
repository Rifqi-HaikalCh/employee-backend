package net.javaguides.springboot.dto;

import lombok.Data;

@Data
public class UserProfileDto {
    private String username;
    private String email;
    private String role;

    public UserProfileDto(String username, String email, String role) {
        this.username = username;
        this.email = email;
        this.role = role;
    }

    public UserProfileDto() {
        // Default constructor
    }
}
