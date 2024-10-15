package qreol.project.userservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import qreol.project.userservice.repository.UserRepository;
import qreol.project.userservice.repository.UserRoleRepository;
import qreol.project.userservice.service.RoleFeignClient;
import qreol.project.userservice.service.UserRoleService;
import qreol.project.userservice.service.UserService;
import qreol.project.userservice.service.impl.UserRoleServiceImpl;
import qreol.project.userservice.service.impl.UserServiceImpl;

@TestConfiguration
@ActiveProfiles("test")
@RequiredArgsConstructor
public class TestConfig {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleFeignClient roleFeignClient;

    @Bean
    @Primary
    public UserService userService() {
        return new UserServiceImpl(userRepository, passwordEncoder);
    }


    @Bean
    @Primary
    public UserRoleService userRoleService() {
        return new UserRoleServiceImpl(roleFeignClient, userService(), userRoleRepository);
    }


}
