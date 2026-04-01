package pfa.dev.employeeservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmployeeLookupResponse {
    private Long id;
    private String userId;
    private String email;
}
