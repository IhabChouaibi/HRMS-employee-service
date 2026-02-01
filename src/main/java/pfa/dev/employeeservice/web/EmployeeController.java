package pfa.dev.employeeservice.web;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pfa.dev.employeeservice.dto.EmployeeDto;
import pfa.dev.employeeservice.dto.EmployeePageResponse;
import pfa.dev.employeeservice.service.EmployeeService;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class EmployeeController {
    private final EmployeeService employeeService;
    @GetMapping("/ping")
    public String ping() {
        return "Employee service is alive";
    }

    @PostMapping("/add")
    public ResponseEntity<EmployeeDto> createEmployee(@RequestBody EmployeeDto employeeDto) {
        return ResponseEntity.ok(employeeService.addEmployee(employeeDto));
    }

    @GetMapping ("listEmployee")
    public ResponseEntity<Page<EmployeePageResponse>> getAllEmployees(@RequestParam(defaultValue = "10") int size , @RequestParam(defaultValue = "0") int page) {
        return ResponseEntity.ok(employeeService.getAllEmployees(PageRequest.of(page, size)));

    }
}
