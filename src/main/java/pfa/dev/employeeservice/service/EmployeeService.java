package pfa.dev.employeeservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pfa.dev.employeeservice.dto.EmployeeDto;
import pfa.dev.employeeservice.dto.EmployeePageResponse;
import pfa.dev.employeeservice.entities.Employee;

public interface EmployeeService {
    EmployeeDto addEmployee(EmployeeDto employee);
    Page<EmployeePageResponse> getAllEmployees(Pageable pageable);
}
