package qreol.project.userservice.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import qreol.project.userservice.model.User;
import qreol.project.userservice.service.UserService;
import qreol.project.userservice.web.validation.flags.OnCreate;
import qreol.project.userservice.web.validation.flags.OnUpdate;
import qreol.project.userservice.web.validation.validators.UserValidator;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserValidator userValidator;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    @PostMapping
    public ResponseEntity<User> createUser(
            @RequestBody @Validated(OnCreate.class) User user,
            BindingResult result
    ) {
        userValidator.validate(user, result);

        return ResponseEntity.ok(userService.create(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUserById(
            @RequestBody @Validated(OnUpdate.class) User user,
            @PathVariable Long id,
            BindingResult result
    ) {
        userValidator.validate(user, result);

        return ResponseEntity.ok(userService.update(user, id));
    }

    @DeleteMapping("/{id}")
    public void deleteUseById(@PathVariable Long id) {
        userService.delete(id);
    }


}
