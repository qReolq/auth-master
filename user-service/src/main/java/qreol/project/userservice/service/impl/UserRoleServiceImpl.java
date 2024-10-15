package qreol.project.userservice.service.impl;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qreol.project.userservice.model.UserRole;
import qreol.project.userservice.model.exception.ResourceNotFoundException;
import qreol.project.userservice.model.exception.RoleException;
import qreol.project.userservice.repository.UserRoleRepository;
import qreol.project.userservice.service.RoleFeignClient;
import qreol.project.userservice.service.UserRoleService;
import qreol.project.userservice.service.UserService;
import qreol.project.userservice.web.dto.Role;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserRoleServiceImpl implements UserRoleService {

    private final RoleFeignClient feignClient;
    private final UserService userService;
    private final UserRoleRepository userRoleRepository;

    @Override
    public List<Role> getRolesByUserId(Long id) {
        userService.getById(id); // Checking exist user or not
        List<Long> roleIds = userRoleRepository
                .findAllRolesIdsFromUserId(id);

        return roleIds.stream()
                .map(feignClient::getRoleById)
                .toList();
    }

    @Override
    @Transactional
    public void assignRoleToUser(Long userId, Long roleId) {
        userService.getById(userId);
        checkRole(roleId);

        Optional<UserRole> userRole = userRoleRepository
                .findByUserIdAndRoleId(userId, roleId);

        if (userRole.isPresent())
            throw new RoleException("Role is already assigned");

        userRoleRepository.save(new UserRole(userId, roleId));
    }

    @Override
    @Transactional
    public void removeRoleFromUser(Long userId, Long roleId) {
        userService.getById(userId);
        checkRole(roleId);

        Optional<UserRole> userRole = userRoleRepository
                .findByUserIdAndRoleId(userId, roleId);

        if (userRole.isEmpty())
            throw new RoleException("User does not have this role");

        userRoleRepository.delete(userRole.get());
    }

    private void checkRole(Long roleId) {
        try {
            feignClient.getRoleById(roleId);
        } catch (FeignException.NotFound e) {
            throw new ResourceNotFoundException("Role with id " + roleId + " is not found");
        }
    }

}
