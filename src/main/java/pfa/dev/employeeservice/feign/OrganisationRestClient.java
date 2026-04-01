package pfa.dev.employeeservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pfa.dev.employeeservice.config.FeignConfig;
import pfa.dev.employeeservice.dto.DepartmentSummaryResponse;
import pfa.dev.employeeservice.dto.JobSummaryResponse;

@FeignClient(name = "organisation-service" ,configuration = FeignConfig.class)
public interface OrganisationRestClient {
    @GetMapping("/jobs/{id}/summary")
    JobSummaryResponse getJobSummaryById(@PathVariable Long id);

    @GetMapping("/dep/{id}/summary")
    DepartmentSummaryResponse getDepartmentSummaryById(@PathVariable Long id);
}
