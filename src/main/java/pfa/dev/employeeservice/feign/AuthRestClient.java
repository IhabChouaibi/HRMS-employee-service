package pfa.dev.employeeservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pfa.dev.employeeservice.config.FeignConfig;
import pfa.dev.employeeservice.dto.AuthCreateUserRequest;
import pfa.dev.employeeservice.dto.AuthCreateUserResponse;

@FeignClient(name = "auth-service", path = "/auth", configuration = FeignConfig.class)
public interface AuthRestClient {

    @PostMapping("/users")
    AuthCreateUserResponse createUser(@RequestBody AuthCreateUserRequest request);

    @DeleteMapping("/users/{userId}")
    void deleteUser(@PathVariable("userId") String userId);
}
