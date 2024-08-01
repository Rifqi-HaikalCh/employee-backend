package net.javaguides.springboot.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@Table(name = "jwt_token")
@Entity
public class JwtToken implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token", nullable = false, unique = true)
    private String token;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "expiry_date", nullable = false)
    private Date expiryDate;

    @Column(name = "is_valid", nullable = false)
    private Boolean isValid;

    public JwtToken(String token, String username, Date expiryDate, Boolean isValid) {
        this.token = token;
        this.username = username;
        this.expiryDate = expiryDate;
        this.isValid = isValid;
    }
}
