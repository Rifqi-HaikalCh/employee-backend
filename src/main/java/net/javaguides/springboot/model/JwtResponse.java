package net.javaguides.springboot.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

@Data
@NoArgsConstructor
public class JwtResponse implements Serializable {

    private String jwttoken;
    private boolean authenticated;
    private String roles;
    private String email; // Add email if it is returned in the response
    private Map<String, Boolean> accessMap;

    // Parameterized constructor for token only
    public JwtResponse(String jwttoken) {
        this.jwttoken = jwttoken;
    }

    // Parameterized constructor with additional fields
    public JwtResponse(String jwttoken, boolean authenticated, String roles, String email, Map<String, Boolean> accessMap) {
        this.jwttoken = jwttoken;
        this.authenticated = authenticated;
        this.roles = roles;
        this.email = email;
        this.accessMap = accessMap;
    }
}
