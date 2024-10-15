package qreol.project.roleservice.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import qreol.project.roleservice.model.Role;
import qreol.project.roleservice.service.RoleService;
import qreol.project.roleservice.web.validation.flags.OnCreate;
import qreol.project.roleservice.web.validation.flags.OnUpdate;
import qreol.project.roleservice.web.validation.validators.RoleValidator;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;
    private final RoleValidator roleValidator;

    @GetMapping
    public ResponseEntity<List<Role>> getAllRoles() {
        log.info("Received request to get all roles");

        List<Role> roles = roleService.getAll();

        log.info("Returning {} roles", roles.size());

        return ResponseEntity.ok(roles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable Long id) {
        log.info("Received request to get role by id: {}", id);

        Role role = roleService.getById(id);

        log.info("Returning role: {}", id);
        return ResponseEntity.ok(role);
    }

    @PostMapping
    public ResponseEntity<Role> createRole(
            @RequestBody @Validated(OnCreate.class) Role role,
            BindingResult result
    ) {
        log.info("Received request to create role: {}", role);
        roleValidator.validate(role, result);

        return ResponseEntity.ok(roleService.create(role));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Role> updateRoleById(
            @RequestBody @Validated(OnUpdate.class) Role role,
            @PathVariable Long id,
            BindingResult result
    ) {
        roleValidator.validate(role, result);

        return ResponseEntity.ok(roleService.updateById(role, id));
    }

    @DeleteMapping("/{id}")
    public void deleteUseById(@PathVariable Long id) {
        roleService.deleteById(id);
    }

}
