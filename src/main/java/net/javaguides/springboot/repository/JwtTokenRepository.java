package net.javaguides.springboot.repository;

import net.javaguides.springboot.model.JwtToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface JwtTokenRepository extends JpaRepository<JwtToken, Long> {
    JwtToken findByToken(String token);
    void deleteByExpiryDateBefore(Date now);
    void deleteByUsername(String username);
    List<JwtToken> findByUsername(String username);

}
