package qreol.project.roleservice.web.controller;

import lombok.RequiredArgsConstructor;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;
    private final RoleValidator roleValidator;

    @GetMapping
    public ResponseEntity<List<Role>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable Long id) {
        return ResponseEntity.ok(roleService.getById(id));
    }

    @PostMapping
    public ResponseEntity<Role> createRole(
            @RequestBody @Validated(OnCreate.class) Role role,
            BindingResult result
    ) {
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
