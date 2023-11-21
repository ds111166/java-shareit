package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.comment.dto.CommentRequestDto;
import ru.practicum.shareit.comment.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserResponseDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.intThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class ItemControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    @MockBean
    private final ItemService mockItemService;

    private final ItemCreateDto itemCreate = ItemCreateDto.builder()
            .name("item")
            .description("description item")
            .available(true)
            .build();
    private final ItemResponseDto item = ItemResponseDto.builder()
            .id(1L)
            .name("item")
            .description("description item")
            .available(true)
            .owner(new UserResponseDto(1L, "owner", "owner@email.ru"))
            .build();

    @Test
    void getOwnerItems() throws Exception {
        when(mockItemService.getOwnerItems(eq(1L), intThat(from -> from > -1), intThat(size -> size > 0)))
                .thenReturn(List.of(item));
        mockMvc.perform(get("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam("from", "0")
                        .queryParam("size", "10")
                        .queryParam("state", "ALL")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(item.getId()), Long.class));

    }

    @Test
    void getItemById() throws Exception {
        when(mockItemService.getItemById(eq(1L), eq(1L)))
                .thenReturn(item);
        mockMvc.perform(get("/items/{itemId}", item.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(item.getId()), Long.class))
                .andExpect(jsonPath("$.name", is("item")))
                .andExpect(jsonPath("$.description", is("description item")))
                .andExpect(jsonPath("$.available", is(true)));
    }

    @Test
    void createItem() throws Exception {
        when(mockItemService.createItem(eq(1L), eq(itemCreate)))
                .thenReturn(item);
        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .content(objectMapper.writeValueAsString(itemCreate)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(item.getId()), Long.class))
                .andExpect(jsonPath("$.name", is("item")))
                .andExpect(jsonPath("$.description", is("description item")))
                .andExpect(jsonPath("$.available", is(true)));
    }

    @Test
    void updateItem() throws Exception {
        ItemResponseDto updatedItem = ItemResponseDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description("new description item")
                .available(item.getAvailable())
                .owner(item.getOwner())
                .build();
        ItemResponseDto updateItem = ItemResponseDto.builder()
                .id(item.getId())
                .description("new description item")
                .build();
        when(mockItemService.updateItem(eq(1L), eq(1L), eq(updateItem)))
                .thenReturn(updatedItem);
        mockMvc.perform(patch("/items/{itemId}", item.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .content(objectMapper.writeValueAsString(updateItem)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(updatedItem.getId()), Long.class))
                .andExpect(jsonPath("$.name", is("item")))
                .andExpect(jsonPath("$.description", is("new description item")))
                .andExpect(jsonPath("$.available", is(true)));
    }

    @Test
    void searchItemsByText() throws Exception {
        when(mockItemService.searchItemsByText(eq("ipt"),
                intThat(from -> from > -1), intThat(size -> size > 0)))
                .thenReturn(List.of(item));
        mockMvc.perform(get("/items/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam("from", "0")
                        .queryParam("size", "10")
                        .queryParam("text", "ipt"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(item.getId()), Long.class));
    }

    @Test
    void createComment() throws Exception {
        CommentRequestDto commentRequestDto = new CommentRequestDto("comment item");
        final LocalDateTime now = LocalDateTime.now();
        CommentResponseDto comment = CommentResponseDto.builder()
                .id(1L)
                .text("comment item")
                .item(item)
                .authorName("user1")
                .created(now)
                .build();
        when(mockItemService.createComment(eq(2L), eq(1L), eq(commentRequestDto))).thenReturn(comment);
        mockMvc.perform(post("/items/{itemId}/comment", item.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 2)
                        .content(objectMapper.writeValueAsString(commentRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(comment.getId()), Long.class))
                .andExpect(jsonPath("$.text", is("comment item")))
                .andExpect(jsonPath("$.authorName", is("user1")))
                .andExpect(jsonPath("$.created",
                        is(comment.getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))));
    }
}