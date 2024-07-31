package net.javaguides.springboot.dto;

import lombok.Data;

@Data
public class UserRoleUpdateDto {
    private Long userId;
    private String roleName;

    // Getters and setters
}
