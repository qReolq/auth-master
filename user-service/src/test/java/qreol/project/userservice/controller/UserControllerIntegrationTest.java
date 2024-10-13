package qreol.project.userservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import qreol.project.userservice.config.TestConfig;
import qreol.project.userservice.model.User;
import qreol.project.userservice.repository.UserRepository;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestConfig.class)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();

        user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("encryptedPassword");
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void getUserByIdWithExistUserTest() throws Exception {
        Long searchId  = userRepository.save(user).getId();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/" + searchId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("testuser")));
    }

    @Test
    void getAllUsersWithOneUserTest() throws Exception {
        userRepository.save(user);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].username", is(user.getUsername())));
    }

    @Test
    void getAllUsersWithoutUsersTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void getUserByIdWithoutExistUserTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("User with id 2 is not found")));
    }

    @Test
    void createUserWithSuccessTest() throws Exception {
        User newUser = new User();
        newUser.setUsername("newuser");
        newUser.setEmail("newuser@example.com");
        newUser.setPassword("encryptedPassword");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(newUser)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(newUser.getUsername())));
    }

    @Test
    void createUserWithEmptyFieldsTest() throws Exception {
        User newUser = new User();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(newUser)))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message", is("Validation failed")));
    }

    @Test
    void createUserWithExistUsernameAndEmailTest() throws Exception {
        userRepository.save(user);
        User withTheSameUsernameAndEmail = new User();
        withTheSameUsernameAndEmail.setUsername(user.getUsername());
        withTheSameUsernameAndEmail.setEmail(user.getEmail());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(withTheSameUsernameAndEmail)))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message", is("Validation failed")));
    }

}
