package qreol.project.roleservice.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import qreol.project.roleservice.config.TestConfig;
import qreol.project.roleservice.model.Role;
import qreol.project.roleservice.model.exception.ResourceNotFoundException;
import qreol.project.roleservice.repository.RoleRepository;
import qreol.project.roleservice.service.impl.RoleServiceImpl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestConfig.class)
@ExtendWith(MockitoExtension.class)
public class RoleServiceTest {

    @MockBean
    private RoleRepository roleRepository;

    @Autowired
    private RoleServiceImpl roleService;

    @Test
    void getByIdExistRoleTest() {
        Long id = 1L;
        Role role = new Role();
        role.setId(id);

        Mockito.when(roleRepository.findById(id)).thenReturn(Optional.of(role));
        Role testRole = roleService.getById(id);

        Mockito.verify(roleRepository).findById(id);
        Assertions.assertEquals(role, testRole);
    }

    @Test
    void getByIdNotExistRole() {
        Long id = 1L;
        String exceptionMessage = "Role with id 1 is not found";
        Role role = new Role();
        role.setId(id);

        Mockito.when(roleRepository.findById(id)).thenReturn(Optional.empty());
        ResourceNotFoundException e = Assertions.assertThrows(
                ResourceNotFoundException.class, () -> roleService.getById(id));

        Mockito.verify(roleRepository).findById(id);
        Assertions.assertEquals(e.getMessage(), exceptionMessage);

    }

    @Test
    void getAllWithRoleTest() {
        Role role = new Role();
        role.setName("admin");

        List<Role> roles = Collections.singletonList(role);
        Mockito.when(roleRepository.findAll()).thenReturn(roles);

        List<Role> result = roleService.getAll();

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(role.getName(), result.get(0).getName());
    }

    @Test
    void getAllWithoutRoleTest() {
        List<Role> roles = Collections.emptyList();
        Mockito.when(roleRepository.findAll()).thenReturn(roles);

        List<Role> result = roleService.getAll();

        Assertions.assertEquals(0, result.size());
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void createRoleTest() {
        Role role = new Role();

        role.setName("admin");

        Mockito.when(roleRepository.save(Mockito.any(Role.class))).thenReturn(role);

        Role createdRole = roleService.create(role);
        Assertions.assertEquals(role.getName(), createdRole.getName());
    }

    @Test
    void updateRoleTest() {
        Role role = new Role();
        role.setName("name");

        Mockito.when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
        Mockito.when(roleRepository.save(Mockito.any(Role.class))).thenReturn(role);

        Role updatedRole = roleService.updateById(role, 1L);
        Assertions.assertNotNull(updatedRole);
        Assertions.assertEquals(role.getName(), updatedRole.getName());
    }

    @Test
    void deleteRole() {
        Role role = new Role();

        Mockito.when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
        roleService.deleteById(1L);

        Mockito.verify(roleRepository, Mockito.times(1)).delete(role);
    }

}
