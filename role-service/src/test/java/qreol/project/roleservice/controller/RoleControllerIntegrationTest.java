package qreol.project.roleservice.controller;

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
import qreol.project.roleservice.config.TestConfig;
import qreol.project.roleservice.model.Role;
import qreol.project.roleservice.repository.RoleRepository;

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
public class RoleControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RoleRepository roleRepository;

    private Role role;

    @BeforeEach
    public void setup() {
        roleRepository.deleteAll();

        role = new Role();
        role.setName("admin");
        role.setDescription("description");
    }

    @Test
    void getRoleByIdWithExistRoleTest() throws Exception {
        Long searchId  = roleRepository.save(role).getId();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/roles/" + searchId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("admin")));
    }

    @Test
    void getAllRolesWithOneRoleTest() throws Exception {
        roleRepository.save(role);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/roles")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(role.getName())));
    }

    @Test
    void getAllRolesWithoutRoleTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/roles")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void getRoleByIdWithoutExistRoleTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/roles/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Role with id 2 is not found")));
    }

    @Test
    void createRoleWithSuccessTest() throws Exception {
        Role role = new Role();
        role.setName("admin");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(role)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(role.getName())));
    }

    @Test
    void createRoleWithEmptyFieldsTest() throws Exception {
        Role role = new Role();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(role)))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message", is("Validation failed")));
    }

    @Test
    void createRoleWithExistName() throws Exception {
        roleRepository.save(role);
        Role withTheSameName = new Role();
        withTheSameName.setName(role.getName());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(withTheSameName)))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message", is("Validation failed")));
    }

}
