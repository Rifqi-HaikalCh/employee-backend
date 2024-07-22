package net.javaguides.springboot.dto;

import lombok.Data;

@Data
public class UserRoleDto {
    private Long id;
    private String username;
    private String role;

    public UserRoleDto(Long id, String username, String role) {
        this.id = id;
        this.username = username;
        this.role = role;
    }
}
