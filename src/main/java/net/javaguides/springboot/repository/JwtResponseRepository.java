package net.javaguides.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import net.javaguides.springboot.model.JwtResponse;

@Repository
public interface JwtResponseRepository extends JpaRepository<JwtResponse, Long> {
}
