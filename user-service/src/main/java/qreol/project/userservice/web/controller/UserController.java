package qreol.project.userservice.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import qreol.project.userservice.model.User;
import qreol.project.userservice.repository.mapper.UserMapper;
import qreol.project.userservice.service.UserService;
import qreol.project.userservice.web.dto.UserResponseDto;
import qreol.project.userservice.web.validation.flags.OnCreate;
import qreol.project.userservice.web.validation.flags.OnUpdate;
import qreol.project.userservice.web.validation.validators.UserValidator;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserMapper userMapper;
    private final UserService userService;
    private final UserValidator userValidator;

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        log.info("Received request to get all users");

        List<UserResponseDto> users = userService.getAll().stream()
                .map(userMapper::toDto)
                .toList();

        log.info("Returning {} users", users.size());

        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        log.info("Received request to get user with id: {}", id);

        UserResponseDto userResponse = userMapper.toDto(userService.getById(id));
        log.info("Returning user: {}", userResponse);

        return ResponseEntity.ok(userResponse);
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(
            @RequestBody @Validated(OnCreate.class) User user,
            BindingResult result
    ) {
        log.info("Received request to create user: {}", user);

        userValidator.validate(user, result);
        UserResponseDto userResponse = userMapper.toDto(userService.create(user));

        log.info("User created successfully with id: {}", userResponse.getId());
        return ResponseEntity.ok(userResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUserById(
            @RequestBody @Validated(OnUpdate.class) User user,
            @PathVariable Long id,
            BindingResult result
    ) {
        log.info("Received request to update user with id: {}", id);

        userValidator.validate(user, result);
        UserResponseDto updatedUser = userMapper.toDto(userService.update(user, id));

        log.info("User with id: {} updated successfully", id);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public void deleteUseById(@PathVariable Long id) {
        log.info("Received request to delete user with id: {}", id);
        userService.delete(id);
        log.info("User with id: {} deleted successfully", id);
    }


}
