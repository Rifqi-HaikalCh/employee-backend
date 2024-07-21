package net.javaguides.springboot.dto;

import lombok.Data;
import net.javaguides.springboot.model.RoleEntity;

@Data
public class UserDto {
    private String username;
    private String email;
    private String password;
    private String confirmation;
    private RoleEntity role;
}
