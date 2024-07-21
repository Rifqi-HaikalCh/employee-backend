package net.javaguides.springboot.service;

import net.javaguides.springboot.model.RoleEntity;

public interface RoleService {
    RoleEntity findByName(String name);
}
