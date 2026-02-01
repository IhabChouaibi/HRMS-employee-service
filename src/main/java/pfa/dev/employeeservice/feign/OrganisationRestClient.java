package pfa.dev.employeeservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pfa.dev.employeeservice.config.FeignConfig;
import pfa.dev.employeeservice.models.Departement;
import pfa.dev.employeeservice.models.Job;

@FeignClient(name = "organisation-service" ,configuration = FeignConfig.class)
public interface OrganisationRestClient {
    @GetMapping("/jobs/get/{id}")
    Job getJobById(@PathVariable  Long id);



    @GetMapping("/dep/get/{id}")
    Departement getDepartmentById(@PathVariable Long id);
}
