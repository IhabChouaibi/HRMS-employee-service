package pfa.dev.employeeservice.dto;

import lombok.Data;

@Data
public class EmployeePageResponse {

    private Long id;
    private String userId;

    private String firstName;
    private String lastName;
    private String email;

    private String jobTitle;
    private String departmentCode;
}
