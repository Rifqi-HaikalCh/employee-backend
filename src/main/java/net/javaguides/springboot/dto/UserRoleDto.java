package net.javaguides.springboot.dto;

// UserRoleDto.java
import lombok.Data;

@Data
public class UserRoleDto {
    private Long id;
    private String username;
    private Roles roles;

    @Data
    public static class Roles {
        private boolean user;
        private boolean superAdmin;
        private boolean staffAdmin;
        private boolean controlAdmin;
    }

    public UserRoleDto(Long id, String username, Roles roles) {
        this.id = id;
        this.username = username;
        this.roles = roles;
    }
}