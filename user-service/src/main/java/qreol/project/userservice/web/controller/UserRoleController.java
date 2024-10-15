package qreol.project.userservice.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import qreol.project.userservice.service.UserRoleService;
import qreol.project.userservice.web.dto.Role;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserRoleController {

    private final UserRoleService userRoleService;

    @GetMapping("/{userId}/roles")
    public ResponseEntity<List<Role>> getUserRoles(@PathVariable Long userId) {
        return ResponseEntity.ok(userRoleService.getRolesByUserId(userId));
    }

    @PostMapping("/{userId}/roles/{roleId}")
    public ResponseEntity<Void> assignRoleToUser(@PathVariable Long userId, @PathVariable Long roleId) {
        userRoleService.assignRoleToUser(userId, roleId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{userId}/roles/{roleId}")
    public ResponseEntity<Void> removeRoleFromUser(@PathVariable Long userId, @PathVariable Long roleId) {
        userRoleService.removeRoleFromUser(userId, roleId);
        return ResponseEntity.noContent().build();
    }
}
