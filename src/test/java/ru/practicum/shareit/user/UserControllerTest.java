package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.service.UserService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class UserControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final UserService userService;

    @Test
    void getUsers() {
    }

    @Test
    void getUserById() throws Exception {
        final UserResponseDto user = shouldCreateUser();

    }

    @Test
    void createUserTest() throws Exception {
        shouldCreateUser();
        shouldNotCreateUserWithNullName();
        shouldNotCreateUserWithWithAnIncorrectEmail();
        shouldNotCreateUserWithNonUniqueEmail();
    }

    @Test
    void updateUser() {
    }

    @Test
    void deleteUser() {
    }

    private void shouldNotCreateUserWithNonUniqueEmail() throws Exception {
        UserRequestDto newUser = UserRequestDto.builder().name("1name3").email("email1@mail.com").build();
        final ResultActions resultActions = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isConflict());
    }

    private void shouldNotCreateUserWithNullName() throws Exception {
        UserRequestDto newUser = UserRequestDto.builder().email("email2@mail.com").build();
        final ResultActions resultActions = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isBadRequest());
    }

    private void shouldNotCreateUserWithWithAnIncorrectEmail() throws Exception {
        UserRequestDto newUser = UserRequestDto.builder().name("name2").email("email1.mail.com").build();
        final ResultActions resultActions = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isBadRequest());

    }

    private UserResponseDto shouldCreateUser() throws Exception {
        UserRequestDto newUser = UserRequestDto.builder().name("name1").email("email1@mail.com").build();
        final ResultActions resultActions = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("name1"))
                .andExpect(jsonPath("$.email").value("email1@mail.com"));
        final MvcResult mvcResult = resultActions.andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        final UserResponseDto createdUser = objectMapper.readValue(response, UserResponseDto.class);
        resultActions.andExpect(jsonPath("$.id").value(createdUser.getId()));
        return createdUser;
    }
}