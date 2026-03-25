package pfa.dev.employeeservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import pfa.dev.employeeservice.dto.EmployeeDto;
import pfa.dev.employeeservice.entities.Employee;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {


        @Mappings({
                @Mapping(source = "id", target = "id"),
                @Mapping(source = "userId", target = "userId"),
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
        EmployeeDto toEmployeeDto(Employee employee);

        @Mappings({
                @Mapping(source = "id", target = "id"),
                @Mapping(source = "firstName", target = "firstName"),
                @Mapping(source = "lastName", target = "lastName"),
                @Mapping(source = "password", target = "password"),
                @Mapping(source = "email", target = "email"),

                @Mapping(source = "phone", target = "phone"),
                @Mapping(source = "hireDate", target = "hireDate"),
                @Mapping(source = "status", target = "status"),
                @Mapping(source = "departmentId", target = "departmentId"),
                @Mapping(source = "jobId", target = "jobId"),
                @Mapping(target = "userId", ignore = true)
        })
        Employee toEmployee(EmployeeDto employeeDto);
    }

