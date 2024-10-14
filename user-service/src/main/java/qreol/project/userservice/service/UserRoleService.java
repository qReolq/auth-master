package qreol.project.userservice.service;

import qreol.project.userservice.web.dto.Role;

import java.util.List;

public interface UserRoleService {

    List<Role> getRolesByUserId(Long id);

    void assignRoleToUser(Long userId, Long roleId);

    void removeRoleFromUser(Long userId, Long roleId);

}
