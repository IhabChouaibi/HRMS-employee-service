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

    public EmployeeDto addEmployee(EmployeeDto dto) {
        Employee employee = employeeMapper.toEmployee(dto);
        employeeRepository.save(employee);
        Job job = organisationRestClient.getJobById(employee.getId());
        Departement departement = organisationRestClient.getDepartmentById(employee.getId());

        EmployeeCreatedEvent event = new EmployeeCreatedEvent(
                employee.getId(),
                employee.getUserId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getPassword(),
                employee.getEmail(),
                employee.getPhone(),
                employee.getHireDate(),
                employee.getStatus(),
                job.getId(),
                departement.getId()
        );

        employeeProducer.sendEmployeeCreated(event);

        return employeeMapper.toEmployeeDto(employee);
    }
    @Override
    public Page<EmployeePageResponse> getAllEmployees(Pageable pageable) {

        return employeeRepository.findAll(pageable)
                .map(employee -> {

                    Job job = organisationRestClient
                            .getJobById(employee.getJobId());

                    Departement department = organisationRestClient
                            .getDepartmentById(employee.getDepartmentId());

                    EmployeePageResponse dto = new EmployeePageResponse();
                    dto.setId(employee.getId());
                    dto.setUserId(employee.getUserId());
                    dto.setFirstName(employee.getFirstName());
                    dto.setLastName(employee.getLastName());
                    dto.setEmail(employee.getEmail());

                    dto.setJobTitle(job.getTitle());
                    dto.setDepartmentCode(department.getCode());

                    return dto;
                });
    }

}
