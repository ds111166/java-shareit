package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    @MockBean
    private final UserService mockUserService;

    private final UserResponseDto user = new UserResponseDto(1L, "user1", "user1@email.ru");
    private final UserRequestDto userRequest = new UserRequestDto("user1", "user1@email.ru");
    private final List<UserResponseDto> users = List.of(
            new UserResponseDto(1L, "user1", "user1@email.ru"),
            new UserResponseDto(2L, "user2", "user2@email.ru"),
            new UserResponseDto(3L, "user3", "user3@email.ru"),
            new UserResponseDto(4L, "user4", "user4@email.ru"));

    @Test
    void createUser() throws Exception {
        when(mockUserService.createUser(eq(userRequest)))
                .thenReturn(user);

        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(user.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect(jsonPath("$.email", is(user.getEmail())));
    }

    @Test
    void getUser() throws Exception {
        when(mockUserService.getUserById(eq(1L)))
                .thenReturn(user);

        mockMvc.perform(get("/users/{id}", user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect(jsonPath("$.email", is(user.getEmail())));
    }

    @Test
    void deleteUser() throws Exception {
        mockMvc.perform(delete("/users/{id}", user.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void getUsers() throws Exception {
        when(mockUserService.getUsers())
                .thenReturn(users);
        mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(content().json(objectMapper.writeValueAsString(users)));
    }

    @Test
    void updateUser() throws Exception {
        when(mockUserService.updateUser(eq(1L), eq(user)))
                .thenReturn(user);

        mockMvc.perform(patch("/users/{id}", user.getId())
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(user.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect(jsonPath("$.email", is(user.getEmail())));
    }
}
