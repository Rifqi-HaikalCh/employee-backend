package net.javaguides.springboot.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "jwt_response")
public class JwtResponse implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jwttoken", nullable = false)
    private String jwttoken;

    // Default constructor
    public JwtResponse() {
    }

    // Parameterized constructor
    public JwtResponse(String jwttoken) {
        this.jwttoken = jwttoken;
    }

// Getter and Setter
}
