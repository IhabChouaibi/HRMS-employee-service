package pfa.dev.employeeservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pfa.dev.employeeservice.dto.EmployeeDto;
import pfa.dev.employeeservice.dto.EmployeePageResponse;

public interface EmployeeService {
    EmployeeDto createEmployee(EmployeeDto employee);
    EmployeeDto getEmployeeById(Long id);
    EmployeeDto updateEmployee(Long id, EmployeeDto employee);
    void deleteEmployee(Long id);
    Page<EmployeePageResponse> getAllEmployees(Pageable pageable);
    Page<EmployeePageResponse> searchEmployees(String keyword, Pageable pageable);
}
