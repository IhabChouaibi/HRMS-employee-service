package pfa.dev.employeeservice.dto;

import lombok.Data;

@Data
public class AuthCreateUserResponse {
    private String keycloakUserId;
    private String username;
    private String email;
}
