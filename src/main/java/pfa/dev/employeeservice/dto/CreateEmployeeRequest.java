package pfa.dev.employeeservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateEmployeeRequest {
    @NotBlank(message = "username is required")
    private String username;

    @NotBlank(message = "password is required")
    private String password;

    @Email(message = "email must be valid")
    @NotBlank(message = "email is required")
    private String email;

    private String firstName;
    private String lastName;
    private String phone;
    private LocalDate hireDate;
    private String status;

    @NotNull(message = "departmentId is required")
    private Long departmentId;

    private String departmentCode;

    @NotNull(message = "jobId is required")
    private Long jobId;

    private String jobTitle;
}
