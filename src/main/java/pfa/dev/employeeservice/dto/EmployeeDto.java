package pfa.dev.employeeservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
@Getter
@Setter
@ToString
@Builder
public class EmployeeDto {
    private Long id;
    private String userId;
    private String password;

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDate hireDate;
    private String status;

    private Long departmentId;
    private Long jobId;
}
