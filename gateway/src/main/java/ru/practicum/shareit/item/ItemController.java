package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.validation.Marker;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemClient itemClient;

    @GetMapping
    public ResponseEntity<Object> getOwnerItems(
            @RequestHeader("X-Sharer-User-Id") Long ownerId,
            @Min(value = 0, message = "Индекс первого элемента не должен быть меньше нуля!")
            @RequestParam(value = "from", defaultValue = "0") Integer from,
            @Min(value = 1, message = "Количество элементов для отображения не должно быть меньше единицы!")
            @RequestParam(value = "size", required = false) Integer size) {
        log.info("Get items by owner id={}, from={}, size={}", ownerId, from, size);
        return itemClient.getOwnerItems(ownerId, from, (size == null) ? Integer.MAX_VALUE : size);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable @NotNull Long itemId) {
        log.info("Get item by id={}, userId={}", itemId, userId);
        return itemClient.getItemById(userId, itemId);
    }

    @PostMapping
    @Validated({Marker.OnCreate.class})
    public ResponseEntity<Object> createItem(
            @RequestHeader("X-Sharer-User-Id") Long ownerId,
            @Valid @RequestBody ItemCreateDto newItem) {
        log.info("Creating item {}, userId={}", newItem, ownerId);
        return itemClient.createItem(ownerId, newItem);
    }

    @PatchMapping("/{itemId}")
    @Validated({Marker.OnUpdate.class})
    public ResponseEntity<Object> updateItem(
            @RequestHeader("X-Sharer-User-Id") Long ownerId,
            @PathVariable @NotNull Long itemId,
            @Valid @RequestBody ItemResponseDto itemData) {
        log.info("Update item {}, itemId={}, ownerId={}", itemData, itemId, ownerId);
        return itemClient.updateItem(itemData, itemId, ownerId);
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
        return itemClient.searchItemsByText(text, from, (size == null) ? Integer.MAX_VALUE : size);
    }

    @PostMapping("/{itemId}/comment")
    @Validated({Marker.OnCreate.class})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> createComment(
            @RequestHeader("X-Sharer-User-Id") Long authorId,
            @PathVariable @NotNull Long itemId,
            @Valid @RequestBody CommentRequestDto newComment) {
        log.info("Create comment {}, authorId={}, itemId={}", newComment, authorId, itemId);
        return itemClient.createComment(authorId, itemId, newComment);
    }
}
