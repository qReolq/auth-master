package qreol.project.userservice.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import qreol.project.userservice.config.TestConfig;
import qreol.project.userservice.model.User;
import qreol.project.userservice.model.exception.ResourceNotFoundException;
import qreol.project.userservice.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestConfig.class)
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserServiceImpl userService;

    @Test
    void getByIdExistUser() {
        Long id = 1L;
        User user = new User();
        user.setId(id);

        Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(user));
        User testUser = userService.getById(id);

        Mockito.verify(userRepository).findById(id);
        Assertions.assertEquals(user, testUser);
    }

    @Test
    void getByIdNotExistUser() {
        Long id = 1L;
        String exceptionMessage = "User with id 1 is not found";
        User user = new User();
        user.setId(id);

        Mockito.when(userRepository.findById(id)).thenReturn(Optional.empty());
        ResourceNotFoundException e = Assertions.assertThrows(
                ResourceNotFoundException.class, () -> userService.getById(id));

        Mockito.verify(userRepository).findById(id);
        Assertions.assertEquals(e.getMessage(), exceptionMessage);

    }

    @Test
    void getAllWithUserTest() {
        User user = new User();
        user.setUsername("user");

        List<User> users = Collections.singletonList(user);
        Mockito.when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAll();

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(user.getUsername(), result.get(0).getUsername());
    }

    @Test
    void getAllWithoutUserTest() {
        List<User> users = Collections.emptyList();
        Mockito.when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAll();

        Assertions.assertEquals(0, result.size());
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void createUserTest() {
        User user = new User();
        String rawPass = "123";

        user.setUsername("name");

        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
        Mockito.when(passwordEncoder.encode(rawPass)).thenReturn(rawPass);

        User createdUser = userService.create(user);
        Assertions.assertEquals(user.getUsername(), createdUser.getUsername());
    }

    @Test
    void updateUserTest() {
        User user = new User();
        user.setUsername("name");

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        User updatedUser = userService.update(user, 1L);
        Assertions.assertNotNull(updatedUser);
        Assertions.assertEquals(user.getUsername(), updatedUser.getUsername());
    }

    @Test
    void deleteUser() {
        User user = new User();

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        userService.delete(1L);

        Mockito.verify(userRepository, Mockito.times(1)).delete(user);
    }

}
