package qreol.project.roleservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import qreol.project.roleservice.model.Role;
import qreol.project.roleservice.model.exception.ResourceNotFoundException;
import qreol.project.roleservice.repository.RoleRepository;
import qreol.project.roleservice.service.RoleService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public List<Role> getAll() {
        return roleRepository.findAll();
    }

    @Override
    public Role getById(Long id) {
        return roleRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Role with id %s is not found", id))
                );
    }

    @Override
    public Role create(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public Role updateById(Role role, Long id) {
        Role toUpdate = getById(id);

        if (role.getName() != null && !role.getName().equals(toUpdate.getName())) {
            toUpdate.setDescription(role.getDescription());
        }

        if (role.getDescription() != null && !role.getDescription().equals(toUpdate.getDescription())) {
            toUpdate.setDescription(role.getDescription());
        }

        return roleRepository.save(role);
    }

    @Override
    public void deleteById(Long id) {
        Role toDelete = getById(id);

        roleRepository.delete(toDelete);
    }

}
