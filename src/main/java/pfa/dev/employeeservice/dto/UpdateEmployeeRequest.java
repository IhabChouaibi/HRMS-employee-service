package pfa.dev.employeeservice.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateEmployeeRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDate hireDate;
    private String status;
    private Long departmentId;
    private Long jobId;
}
