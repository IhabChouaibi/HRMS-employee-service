package pfa.dev.employeeservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthCreateUserRequest {
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
}
