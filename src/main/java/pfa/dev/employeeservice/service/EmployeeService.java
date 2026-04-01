package pfa.dev.employeeservice.service;

import pfa.dev.employeeservice.dto.CreateEmployeeRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pfa.dev.employeeservice.dto.EmployeeDto;
import pfa.dev.employeeservice.dto.EmployeeLookupResponse;
import pfa.dev.employeeservice.dto.EmployeePageResponse;
import pfa.dev.employeeservice.dto.UpdateEmployeeRequest;

public interface EmployeeService {
    EmployeeDto createEmployee(CreateEmployeeRequest employee);
    EmployeeDto getEmployeeById(Long id);
    EmployeeDto updateEmployee(Long id, UpdateEmployeeRequest employee);
    void deleteEmployee(Long id);
    Page<EmployeePageResponse> getAllEmployees(Pageable pageable);
    Page<EmployeePageResponse> searchEmployees(String keyword, Pageable pageable);
    EmployeeLookupResponse getEmployeeByUserId(String userId);
}
