package net.javaguides.springboot.repository;

import net.javaguides.springboot.model.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

    @Repository
    public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
        Optional<RoleEntity> findByName(String name);
    }
