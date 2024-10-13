package qreol.project.roleservice.service;

import qreol.project.roleservice.model.Role;

import java.util.List;

public interface RoleService {

    List<Role> getAll();

    Role getById(Long id);

    Role create(Role role);

    Role updateById(Role role, Long id);

    void deleteById(Long id);

}
