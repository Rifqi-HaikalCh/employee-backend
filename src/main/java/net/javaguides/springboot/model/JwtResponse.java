package net.javaguides.springboot.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@Entity
@Table(name = "jwt_response")
public class JwtResponse implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jwttoken", nullable = false)
    private String jwttoken;

    @Column(name = "authenticated", nullable = false)
    private boolean authenticated;

    @Column(name = "roles", nullable = false)
    private String roles;

    // Parameterized constructor for token only
    public JwtResponse(String jwttoken) {
        this.jwttoken = jwttoken;
    }

    // Parameterized constructor with additional fields
    public JwtResponse(String jwttoken, boolean authenticated, String roles) {
        this.jwttoken = jwttoken;
        this.authenticated = authenticated;
        this.roles = roles;
    }
}
