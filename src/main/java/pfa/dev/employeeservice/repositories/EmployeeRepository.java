package pfa.dev.employeeservice.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pfa.dev.employeeservice.entities.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Page<Employee> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrUserIdContainingIgnoreCase(
            String firstName,
            String lastName,
            String email,
            String userId,
            Pageable pageable
    );
}
