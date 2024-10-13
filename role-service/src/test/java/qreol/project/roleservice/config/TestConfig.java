package qreol.project.roleservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;
import qreol.project.roleservice.repository.RoleRepository;
import qreol.project.roleservice.service.RoleService;
import qreol.project.roleservice.service.impl.RoleServiceImpl;

@TestConfiguration
@ActiveProfiles("test")
@RequiredArgsConstructor
public class TestConfig {

    @Autowired
    private RoleRepository roleRepository;

    @Bean
    @Primary
    public RoleService roleService() {
        return new RoleServiceImpl(roleRepository);
    }

}
