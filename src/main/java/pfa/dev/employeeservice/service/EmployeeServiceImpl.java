package pfa.dev.employeeservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pfa.dev.employeeservice.dto.EmployeeDto;
import pfa.dev.employeeservice.dto.EmployeePageResponse;
import pfa.dev.employeeservice.entities.Employee;
import pfa.dev.employeeservice.event.EmployeeCreatedEvent;
import pfa.dev.employeeservice.exception.ResourceNotFoundException;
import pfa.dev.employeeservice.feign.OrganisationRestClient;
import pfa.dev.employeeservice.kafka.EmployeeProducer;
import pfa.dev.employeeservice.mapper.EmployeeMapper;
import pfa.dev.employeeservice.models.Departement;
import pfa.dev.employeeservice.models.Job;
import pfa.dev.employeeservice.repositories.EmployeeRepository;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final EmployeeProducer employeeProducer;
    private final OrganisationRestClient organisationRestClient;

    @Transactional
    @Override
    public EmployeeDto createEmployee(EmployeeDto dto) {
        Employee employee = employeeMapper.toEmployee(dto);
        employee.setUserId(dto.getUserId());
        Employee savedEmployee = employeeRepository.save(employee);

        Job job = getJob(savedEmployee.getJobId());
        Departement departement = getDepartment(savedEmployee.getDepartmentId());

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
                job != null ? job.getId() : null,
                departement != null ? departement.getId() : null
        );

        employeeProducer.sendEmployeeCreated(event);

        return employeeMapper.toEmployeeDto(savedEmployee);
    }

    @Override
    public EmployeeDto getEmployeeById(Long id) {
        return employeeMapper.toEmployeeDto(findEmployee(id));
    }

    @Transactional
    @Override
    public EmployeeDto updateEmployee(Long id, EmployeeDto dto) {
        Employee employee = findEmployee(id);

        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setUserId(dto.getUserId());
        employee.setEmail(dto.getEmail());
        employee.setPhone(dto.getPhone());
        employee.setHireDate(dto.getHireDate());
        employee.setStatus(dto.getStatus());
        employee.setDepartmentId(dto.getDepartmentId());
        employee.setJobId(dto.getJobId());

        return employeeMapper.toEmployeeDto(employeeRepository.save(employee));
    }

    @Transactional
    @Override
    public void deleteEmployee(Long id) {
        Employee employee = findEmployee(id);
        employeeRepository.delete(employee);
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

    private Employee findEmployee(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
    }

    private EmployeePageResponse toPageResponse(Employee employee) {
        Job job = getJob(employee.getJobId());
        Departement department = getDepartment(employee.getDepartmentId());

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

    private Job getJob(Long jobId) {
        if (jobId == null) {
            return null;
        }
        return organisationRestClient.getJobById(jobId);
    }

    private Departement getDepartment(Long departmentId) {
        if (departmentId == null) {
            return null;
        }
        return organisationRestClient.getDepartmentById(departmentId);
    }
}
