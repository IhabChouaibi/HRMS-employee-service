package pfa.dev.employeeservice.event;
import java.time.LocalDate;

public record EmployeeCreatedEvent(
        Long id,
        String userId,
        String firstName,
        String lastName,
        String password,
        String email,
        String phone,
        LocalDate hireDate,
        String status,
        Long departmentId,
        Long jobId
) {
}
