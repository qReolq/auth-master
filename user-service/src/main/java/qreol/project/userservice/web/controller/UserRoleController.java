package qreol.project.userservice.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import qreol.project.userservice.service.UserRoleService;
import qreol.project.userservice.web.dto.Role;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserRoleController {

    private final UserRoleService userRoleService;

    @GetMapping("/{userId}/roles")
    public ResponseEntity<List<Role>> getUserRoles(@PathVariable Long userId) {
        log.info("Received request to get roles for user with id: {}", userId);

        List<Role> roles = userRoleService.getRolesByUserId(userId);
        log.info("Returning {} roles for user with id: {}", roles.size(), userId);

        return ResponseEntity.ok(roles);
    }

    @PostMapping("/{userId}/roles/{roleId}")
    public ResponseEntity<Void> assignRoleToUser(@PathVariable Long userId, @PathVariable Long roleId) {
        log.info("Received request to assign role with id: {} to user with id: {}", roleId, userId);

        userRoleService.assignRoleToUser(userId, roleId);
        log.info("Role with id: {} assigned to user with id: {}", roleId, userId);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{userId}/roles/{roleId}")
    public ResponseEntity<Void> removeRoleFromUser(@PathVariable Long userId, @PathVariable Long roleId) {
        log.info("Received request to remove role with id: {} from user with id: {}", roleId, userId);

        userRoleService.removeRoleFromUser(userId, roleId);
        log.info("Role with id: {} removed from user with id: {}", roleId, userId);

        return ResponseEntity.noContent().build();
    }
}
