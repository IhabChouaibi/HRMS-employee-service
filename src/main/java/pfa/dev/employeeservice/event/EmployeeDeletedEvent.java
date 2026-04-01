package pfa.dev.employeeservice.event;

public record EmployeeDeletedEvent(
        Long employeeId,
        String userId,
        String email
) {
}
