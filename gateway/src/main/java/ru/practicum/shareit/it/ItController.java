package ru.practicum.shareit.it;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CommentRequestDto;
import ru.practicum.shareit.it.dto.ItCreateDto;
import ru.practicum.shareit.it.dto.ItResponseDto;
import ru.practicum.shareit.validation.Marker;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/items")
public class ItController {

    private final ItClient itClient;

    @GetMapping
    public ResponseEntity<Object> getOwnerItems(
            @RequestHeader("X-Sharer-User-Id") Long ownerId,
            @Min(value = 0, message = "Индекс первого элемента не должен быть меньше нуля!")
            @RequestParam(value = "from", defaultValue = "0") Integer from,
            @Min(value = 1, message = "Количество элементов для отображения не должно быть меньше единицы!")
            @RequestParam(value = "size", required = false) Integer size) {
        log.info("Get items by owner id={}, from={}, size={}", ownerId, from, size);
        return itClient.getOwnerItems(ownerId, from, size);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable @NotNull Long itemId) {
        log.info("Get item by id={}, userId={}", itemId, userId);
        return itClient.getItemById(userId, itemId);
    }

    @PostMapping
    @Validated({Marker.OnCreate.class})
    public ResponseEntity<Object> createItem(
            @RequestHeader("X-Sharer-User-Id") Long ownerId,
            @Valid @RequestBody ItCreateDto newItem) {
        log.info("Creating item {}, userId={}", newItem, ownerId);
        return itClient.createItem(ownerId, newItem);
    }

    @PatchMapping("/{itemId}")
    @Validated({Marker.OnUpdate.class})
    public ResponseEntity<Object> updateItem(
            @RequestHeader("X-Sharer-User-Id") Long ownerId,
            @PathVariable @NotNull Long itemId,
            @Valid @RequestBody ItResponseDto itemData) {
        log.info("Update item {}, itemId={}, ownerId={}", itemData, itemId, ownerId);
        return itClient.updateItem(itemData, itemId, ownerId);
    }

    @GetMapping("/search")
    @Validated
    public ResponseEntity<Object> searchItemsByText(
            @RequestParam("text") String text,
            @Min(value = 0, message = "Индекс первого элемента не должен быть меньше нуля!")
            @RequestParam(value = "from", defaultValue = "0") Integer from,
            @Min(value = 1, message = "Количество элементов для отображения не должно быть меньше единицы!")
            @RequestParam(value = "size", required = false) Integer size) {
        log.info("Search items by text \"{}\", from={}, size={}", text, from, size);
        return itClient.searchItemsByText(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    @Validated({Marker.OnCreate.class})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> createComment(
            @RequestHeader("X-Sharer-User-Id") Long authorId,
            @PathVariable @NotNull Long itemId,
            @Valid @RequestBody CommentRequestDto newComment) {
        log.info("Create comment {}, authorId={}, itemId={}", newComment, authorId, itemId);
        return itClient.createComment(authorId, itemId, newComment);
    }
}
