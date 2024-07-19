package net.javaguides.springboot.service;

import net.javaguides.springboot.model.RoleEntity;
import net.javaguides.springboot.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public RoleEntity findByName(String name) {
        return roleRepository.findByName(name).get();
    }

    // Additional methods if needed
}
