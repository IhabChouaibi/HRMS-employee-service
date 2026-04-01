package pfa.dev.employeeservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import pfa.dev.employeeservice.dto.CreateEmployeeRequest;
import pfa.dev.employeeservice.dto.EmployeeDto;
import pfa.dev.employeeservice.dto.UpdateEmployeeRequest;
import pfa.dev.employeeservice.entities.Employee;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {


        @Mappings({
                @Mapping(source = "id", target = "id"),
                @Mapping(source = "userId", target = "userId"),
                @Mapping(source = "firstName", target = "firstName"),
                @Mapping(source = "lastName", target = "lastName"),
                @Mapping(source = "email", target = "email"),
                @Mapping(source = "phone", target = "phone"),
                @Mapping(source = "hireDate", target = "hireDate"),
                @Mapping(source = "status", target = "status"),
                @Mapping(target = "jobTitle", ignore = true),
                @Mapping(target = "departmentCode", ignore = true)
        })
        EmployeeDto toEmployeeDto(Employee employee);

        @Mappings({
                @Mapping(target = "id", ignore = true),
                @Mapping(target = "userId", ignore = true),
                @Mapping(source = "firstName", target = "firstName"),
                @Mapping(source = "lastName", target = "lastName"),
                @Mapping(source = "password", target = "password"),
                @Mapping(source = "email", target = "email"),
                @Mapping(source = "phone", target = "phone"),
                @Mapping(source = "hireDate", target = "hireDate"),
                @Mapping(source = "status", target = "status"),
                @Mapping(source = "departmentId", target = "departmentId"),
                @Mapping(source = "jobId", target = "jobId")
        })
        Employee toEmployee(CreateEmployeeRequest request);

        @Mappings({
                @Mapping(target = "id", ignore = true),
                @Mapping(target = "userId", ignore = true),
                @Mapping(target = "password", ignore = true),
                @Mapping(source = "firstName", target = "firstName"),
                @Mapping(source = "lastName", target = "lastName"),
                @Mapping(source = "email", target = "email"),
                @Mapping(source = "phone", target = "phone"),
                @Mapping(source = "hireDate", target = "hireDate"),
                @Mapping(source = "status", target = "status"),
                @Mapping(source = "departmentId", target = "departmentId"),
                @Mapping(source = "jobId", target = "jobId")
        })
        void updateEmployeeFromRequest(UpdateEmployeeRequest request, @MappingTarget Employee employee);
}

