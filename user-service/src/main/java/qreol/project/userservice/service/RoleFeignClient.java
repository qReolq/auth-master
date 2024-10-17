package qreol.project.userservice.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import qreol.project.userservice.web.dto.Role;

@Component
@FeignClient(name = "role-service", url = "http://api-gateway:8765/role-service/api/roles")
public interface RoleFeignClient {

    @GetMapping("/{id}")
    Role getRoleById(@PathVariable Long id);

}