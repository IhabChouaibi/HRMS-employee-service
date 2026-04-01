package pfa.dev.employeeservice.service;

import jakarta.transaction.Transactional;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pfa.dev.employeeservice.dto.AuthCreateUserRequest;
import pfa.dev.employeeservice.dto.AuthCreateUserResponse;
import pfa.dev.employeeservice.dto.CreateEmployeeRequest;
import pfa.dev.employeeservice.dto.DepartmentSummaryResponse;
import pfa.dev.employeeservice.dto.EmployeeDto;
import pfa.dev.employeeservice.dto.EmployeeLookupResponse;
import pfa.dev.employeeservice.dto.EmployeePageResponse;
import pfa.dev.employeeservice.dto.JobSummaryResponse;
import pfa.dev.employeeservice.dto.UpdateEmployeeRequest;
import pfa.dev.employeeservice.entities.Employee;
import pfa.dev.employeeservice.event.EmployeeCreatedEvent;
import pfa.dev.employeeservice.event.EmployeeDeletedEvent;
import pfa.dev.employeeservice.exception.ResourceNotFoundException;
import pfa.dev.employeeservice.feign.AuthRestClient;
import pfa.dev.employeeservice.feign.OrganisationRestClient;
import pfa.dev.employeeservice.kafka.EmployeeProducer;
import pfa.dev.employeeservice.mapper.EmployeeMapper;
import pfa.dev.employeeservice.repositories.EmployeeRepository;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final EmployeeProducer employeeProducer;
    private final AuthRestClient authRestClient;
    private final OrganisationRestClient organisationRestClient;

    @Transactional
    @Override
    public EmployeeDto createEmployee(CreateEmployeeRequest request) {
        AuthCreateUserResponse createdUser = authRestClient.createUser(AuthCreateUserRequest.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(request.getPassword())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .build());

        Employee employee = employeeMapper.toEmployee(request);
        employee.setUserId(createdUser.getKeycloakUserId());
        // jobTitle and departmentCode may be provided by the client for display purposes,
        // but persistence still relies on jobId and departmentId only.

        Employee savedEmployee;
        try {
            savedEmployee = employeeRepository.save(employee);
        } catch (RuntimeException exception) {
            rollbackCreatedKeycloakUser(createdUser.getKeycloakUserId(), exception);
            throw exception;
        }

        EmployeeCreatedEvent event = new EmployeeCreatedEvent(
                savedEmployee.getId(),
                savedEmployee.getUserId(),
                savedEmployee.getFirstName(),
                savedEmployee.getLastName(),
                savedEmployee.getPassword(),
                savedEmployee.getEmail(),
                savedEmployee.getPhone(),
                savedEmployee.getHireDate(),
                savedEmployee.getStatus(),
                savedEmployee.getDepartmentId(),
                savedEmployee.getJobId()
        );

        employeeProducer.sendEmployeeCreated(event);

        return toEmployeeResponse(savedEmployee);
    }

    @Override
    public EmployeeDto getEmployeeById(Long id) {
        return toEmployeeResponse(findEmployee(id));
    }

    @Transactional
    @Override
    public EmployeeDto updateEmployee(Long id, UpdateEmployeeRequest dto) {
        Employee employee = findEmployee(id);
        employeeMapper.updateEmployeeFromRequest(dto, employee);
        return toEmployeeResponse(employeeRepository.save(employee));
    }

    @Transactional
    @Override
    public void deleteEmployee(Long id) {
        Employee employee = findEmployee(id);
        employeeRepository.delete(employee);
        employeeRepository.flush();
        employeeProducer.sendEmployeeDeleted(new EmployeeDeletedEvent(
                employee.getId(),
                employee.getUserId(),
                employee.getEmail()
        ));
    }

    @Override
    public Page<EmployeePageResponse> getAllEmployees(Pageable pageable) {
        return employeeRepository.findAll(pageable)
                .map(this::toPageResponse);
    }

    @Override
    public Page<EmployeePageResponse> searchEmployees(String keyword, Pageable pageable) {
        return employeeRepository
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrUserIdContainingIgnoreCase(
                        keyword,
                        keyword,
                        keyword,
                        keyword,
                        pageable
                )
                .map(this::toPageResponse);
    }

    @Override
    public EmployeeLookupResponse getEmployeeByUserId(String userId) {
        Employee employee = employeeRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with userId: " + userId));

        return EmployeeLookupResponse.builder()
                .id(employee.getId())
                .userId(employee.getUserId())
                .email(employee.getEmail())
                .build();
    }

    private Employee findEmployee(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
    }

    private EmployeePageResponse toPageResponse(Employee employee) {
        JobSummaryResponse job = getJobSummary(employee.getJobId());
        DepartmentSummaryResponse department = getDepartmentSummary(employee.getDepartmentId());

        EmployeePageResponse dto = new EmployeePageResponse();
        dto.setId(employee.getId());
        dto.setUserId(employee.getUserId());
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setEmail(employee.getEmail());
        dto.setJobTitle(job != null ? job.getTitle() : null);
        dto.setDepartmentCode(department != null ? department.getCode() : null);
        return dto;
    }

    private EmployeeDto toEmployeeResponse(Employee employee) {
        JobSummaryResponse job = getJobSummary(employee.getJobId());
        DepartmentSummaryResponse department = getDepartmentSummary(employee.getDepartmentId());

        EmployeeDto employeeDto = employeeMapper.toEmployeeDto(employee);
        employeeDto.setJobTitle(job != null ? job.getTitle() : null);
        employeeDto.setDepartmentCode(department != null ? department.getCode() : null);
        return employeeDto;
    }

    private JobSummaryResponse getJobSummary(Long jobId) {
        if (jobId == null) {
            return null;
        }
        return organisationRestClient.getJobSummaryById(jobId);
    }

    private DepartmentSummaryResponse getDepartmentSummary(Long departmentId) {
        if (departmentId == null) {
            return null;
        }
        return organisationRestClient.getDepartmentSummaryById(departmentId);
    }

    private void rollbackCreatedKeycloakUser(String userId, RuntimeException originalException) {
        if (userId == null || userId.isBlank()) {
            return;
        }

        try {
            authRestClient.deleteUser(userId);
        } catch (FeignException exception) {
            originalException.addSuppressed(new IllegalStateException(
                    "Employee persistence failed after Keycloak user creation. Manual cleanup may be required for userId: " + userId,
                    exception
            ));
        }
    }
}
