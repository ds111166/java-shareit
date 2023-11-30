package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.dto.UserResponseDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.intThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class ItemRequestControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    @MockBean
    private final ItemRequestService mockItemRequestService;
    private final LocalDateTime now = LocalDateTime.now();
    private final UserResponseDto requestor = new UserResponseDto(3L, "requester", "requester@email.ru");
    private final ItemRequestResponseDto itemRequest = ItemRequestResponseDto.builder()
            .id(1L)
            .description("item")
            .requestor(requestor)
            .created(now)
            .build();

    @Test
    void createItemRequest() throws Exception {

        ItemRequestCreateDto newItemRequest = ItemRequestCreateDto.builder().description("item").build();
        when(mockItemRequestService.createItemRequest(eq(3L), eq(newItemRequest)))
                .thenReturn(itemRequest);
        mockMvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", requestor.getId())
                        .content(objectMapper.writeValueAsString(newItemRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(itemRequest.getId()), Long.class))
                .andExpect(jsonPath("$.description", is("item")))
                .andExpect(jsonPath("$.created",
                        is(itemRequest.getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))));

    }

    @Test
    void getItemRequestById() throws Exception {
        when(mockItemRequestService.getItemRequestById(eq(3L), eq(1L)))
                .thenReturn(itemRequest);
        mockMvc.perform(get("/requests/{requestId}", itemRequest.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 3))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequest.getId()), Long.class))
                .andExpect(jsonPath("$.description", is("item")))
                .andExpect(jsonPath("$.created",
                        is(itemRequest.getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))));

    }

    @Test
    void getItemRequests() throws Exception {
        when(mockItemRequestService.getItemRequests(eq(requestor.getId())))
                .thenReturn(List.of(itemRequest));
        mockMvc.perform(get("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", requestor.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(itemRequest.getId()), Long.class));
    }

    @Test
    void getItemRequestsAll() throws Exception {
        when(mockItemRequestService.getItemRequestsAll(eq(requestor.getId()),
                intThat(from -> from > -1), intThat(size -> size > 0)))
                .thenReturn(List.of(itemRequest));
        mockMvc.perform(get("/requests/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam("from", "0")
                        .queryParam("size", "10")
                        .header("X-Sharer-User-Id", requestor.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(itemRequest.getId()), Long.class));
    }
}