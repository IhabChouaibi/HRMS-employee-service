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
@RequestMapping("/api/employees")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('HR', 'EMPLOYEE')")

public class EmployeeController {
    private final EmployeeService employeeService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<EmployeeDto> createEmployee(@RequestBody EmployeeDto employeeDto) {
        return ResponseEntity.ok(employeeService.createEmployee(employeeDto));
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

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<EmployeeDto> updateEmployee(@PathVariable Long id, @RequestBody EmployeeDto employeeDto) {
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
