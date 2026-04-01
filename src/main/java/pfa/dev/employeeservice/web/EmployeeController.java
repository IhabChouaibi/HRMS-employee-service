package pfa.dev.employeeservice.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pfa.dev.employeeservice.dto.CreateEmployeeRequest;
import pfa.dev.employeeservice.dto.EmployeeDto;
import pfa.dev.employeeservice.dto.EmployeeLookupResponse;
import pfa.dev.employeeservice.dto.EmployeePageResponse;
import pfa.dev.employeeservice.dto.UpdateEmployeeRequest;
import pfa.dev.employeeservice.service.EmployeeService;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('HR', 'EMPLOYEE')")

public class EmployeeController {
    private final EmployeeService employeeService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<EmployeeDto> createEmployee(@Valid @RequestBody CreateEmployeeRequest request) {
        return ResponseEntity.ok(employeeService.createEmployee(request));
    }

    @GetMapping("/getall")
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<Page<EmployeePageResponse>> getAllEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(employeeService.getAllEmployees(PageRequest.of(page, size)));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    @GetMapping("/by-user-id/{userId}")
    public ResponseEntity<EmployeeLookupResponse> getEmployeeByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(employeeService.getEmployeeByUserId(userId));
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<EmployeeDto> updateEmployee(@PathVariable Long id, @RequestBody UpdateEmployeeRequest employeeDto) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, employeeDto));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<Page<EmployeePageResponse>> searchEmployees(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(employeeService.searchEmployees(keyword, PageRequest.of(page, size)));
    }
}
