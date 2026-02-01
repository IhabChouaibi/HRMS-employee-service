package pfa.dev.employeeservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pfa.dev.employeeservice.entities.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
