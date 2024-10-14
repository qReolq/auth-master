package qreol.project.userservice.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import qreol.project.userservice.model.User;
import qreol.project.userservice.model.UserRole;
import qreol.project.userservice.model.exception.RoleException;
import qreol.project.userservice.repository.UserRoleRepository;
import qreol.project.userservice.service.RoleFeignClient;
import qreol.project.userservice.service.UserRoleService;
import qreol.project.userservice.service.UserService;
import qreol.project.userservice.web.dto.Role;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class UserRoleServiceTest {

    @MockBean
    private UserRoleRepository userRoleRepository;

    @MockBean
    private RoleFeignClient roleFeignClient;

    @MockBean
    private UserService userService;

    @Autowired
    private UserRoleService userRoleService;

    @Test
    void getRolesByUserIdWithRolesTest() {
        Long userId = 1L;
        List<Long> roleIds = List.of(1L,2L);

        when(userService.getById(userId)).thenReturn(new User());
        when(userRoleRepository.findAllRolesIdsFromUserId(userId)).thenReturn(roleIds);

        Role role1 = new Role(1L, "ROLE_ADMIN");
        Role role2 = new Role(2L, "ROLE_USER");

        when(roleFeignClient.getRoleById(1L)).thenReturn(role1);
        when(roleFeignClient.getRoleById(2L)).thenReturn(role2);

        List<Role> roles = userRoleService.getRolesByUserId(userId);

        assertEquals(2, roles.size());
        assertEquals("ROLE_ADMIN", roles.get(0).getName());
        assertEquals("ROLE_USER", roles.get(1).getName());

        verify(userService, times(1)).getById(userId);
        verify(userRoleRepository, times(1)).findAllRolesIdsFromUserId(userId);
        verify(roleFeignClient, times(1)).getRoleById(1L);
        verify(roleFeignClient, times(1)).getRoleById(2L);
    }

    @Test
    void assignRoleToUserSuccessTest() {
        Long userId = 1L;
        Long roleId = 1L;
        User user = new User();
        Role role = new Role();

        when(userService.getById(userId)).thenReturn(user);
        when(roleFeignClient.getRoleById(roleId)).thenReturn(role);
        when(userRoleRepository.save(new UserRole())).thenReturn(new UserRole());
        when(userRoleRepository.findByUserIdAndRoleId(userId, roleId))
                .thenReturn(Optional.empty());

        userRoleService.assignRoleToUser(userId, roleId);

        verify(userService, times(1)).getById(userId);
        verify(roleFeignClient, times(1)).getRoleById(roleId);
        verify(userRoleRepository, times(1)).findByUserIdAndRoleId(userId, roleId);
    }

    @Test
    void assignRoleToUserRoleAlreadyAssignedTest() {
        Long userId = 1L;
        Long roleId = 1L;
        User user = new User();
        Role role = new Role();

        when(userService.getById(userId)).thenReturn(user);
        when(roleFeignClient.getRoleById(roleId)).thenReturn(role);
        when(userRoleRepository.findByUserIdAndRoleId(userId, roleId))
                .thenReturn(Optional.of(new UserRole()));

        RoleException e = assertThrows(RoleException.class,
                () -> userRoleService.assignRoleToUser(userId, roleId));

        Assertions.assertEquals("Role is already assigned", e.getMessage());

        verify(userService, times(1)).getById(userId);
        verify(roleFeignClient, times(1)).getRoleById(roleId);
        verify(userRoleRepository, times(1)).findByUserIdAndRoleId(userId, roleId);

    }

    @Test
    void removeRoleFromUserSuccessTest() {
        Long userId = 1L;
        Long roleId = 1L;
        User user = new User();
        Role role = new Role();
        UserRole userRole = new UserRole();

        when(userService.getById(userId)).thenReturn(user);
        when(roleFeignClient.getRoleById(roleId)).thenReturn(role);
        doNothing().when(userRoleRepository).delete(userRole);
        when(userRoleRepository.findByUserIdAndRoleId(userId, roleId))
                .thenReturn(Optional.of(userRole));

        userRoleService.removeRoleFromUser(userId, roleId);

        verify(userService, times(1)).getById(userId);
        verify(roleFeignClient, times(1)).getRoleById(roleId);
        verify(userRoleRepository, times(1)).findByUserIdAndRoleId(userId, roleId);
    }

    @Test
    void removeRoleFromUserRoleNotAssignedTest() {
        Long userId = 1L;
        Long roleId = 1L;
        User user = new User();
        Role role = new Role();

        when(userService.getById(userId)).thenReturn(user);
        when(roleFeignClient.getRoleById(roleId)).thenReturn(role);
        when(userRoleRepository.findByUserIdAndRoleId(userId, roleId))
                .thenReturn(Optional.empty());

        RoleException e = assertThrows(RoleException.class,
                () ->  userRoleService.removeRoleFromUser(userId, roleId));

        assertEquals(e.getMessage(), "User does not have this role");

        verify(userRoleRepository, never()).save(any(UserRole.class));
    }

}
