package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CommentRequestDto;
import ru.practicum.shareit.comment.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.validation.Marker;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/items")
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemResponseDto> getOwnerItems(
            @RequestHeader("X-Sharer-User-Id") Long ownerId,
            @Min(value = 0, message = "Индекс первого элемента не должен быть меньше нуля!")
            @RequestParam(value = "from", defaultValue = "0") Integer from,
            @Min(value = 1, message = "Количество элементов для отображения не должно быть меньше единицы!")
            @RequestParam(value = "size", required = false) Integer size) {

        log.info("Запрос на получение списка вещей владельца с id: {}." +
                " Индекс первого элемента: {}." +
                " Количество элементов для отображения: {}", ownerId, from, size);
        final List<ItemResponseDto> items = itemService.getOwnerItems(ownerId, from,
                (size == null) ? Integer.MAX_VALUE : size);
        log.info("Количество найденных вещей владельца с id: {} равно: {}", ownerId, items.size());
        log.info("Идентификаторы вещей: \"{}\"",
                items.stream().map(ItemResponseDto::getId).collect(Collectors.toList()));
        return items;
    }

    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemResponseDto getItemById(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable @NotNull Long itemId) {

        log.info("Запрос на получение вещи с id: {} от пользователя с id: {}", itemId, userId);
        final ItemResponseDto itemById = itemService.getItemById(userId, itemId);
        log.info("Отправлена: {}", itemById);
        return itemById;
    }

    @PostMapping
    @Validated({Marker.OnCreate.class})
    @ResponseStatus(HttpStatus.CREATED)
    public ItemResponseDto createItem(
            @RequestHeader("X-Sharer-User-Id") Long ownerId,
            @Valid @RequestBody ItemCreateDto newItem) {

        log.info("Запрос на добавление: {} владельцем с id: {}", newItem, ownerId);
        final ItemResponseDto createdItem = itemService.createItem(ownerId, newItem);
        log.info("Добавлена: {}", createdItem);
        return createdItem;
    }

    @PatchMapping("/{itemId}")
    @Validated({Marker.OnUpdate.class})
    @ResponseStatus(HttpStatus.OK)
    public ItemResponseDto updateItem(
            @RequestHeader("X-Sharer-User-Id") Long ownerId,
            @PathVariable @NotNull Long itemId,
            @Valid @RequestBody ItemResponseDto itemData) {

        log.info("Запрос на обновление: {}, id вещи: {}, id владельца: {}", itemData, itemId, ownerId);
        final ItemResponseDto updatedItem = itemService.updateItem(ownerId, itemId, itemData);
        log.info("Обновлена: {}", updatedItem);
        return updatedItem;
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    @Validated
    public List<ItemResponseDto> searchItemsByText(
            @RequestParam("text") String text,
            @Min(value = 0, message = "Индекс первого элемента не должен быть меньше нуля!")
            @RequestParam(value = "from", defaultValue = "0") Integer from,
            @Min(value = 1, message = "Количество элементов для отображения не должно быть меньше единицы!")
            @RequestParam(value = "size", required = false) Integer size) {

        log.info("Запрос поиска вещей по описанию: \"{}\". Индекс первого элемента: {}." +
                "Количество элементов для отображения: {}", text, from, size);
        final List<ItemResponseDto> items = itemService.searchItemsByText(text, from,
                (size == null) ? Integer.MAX_VALUE : size);
        log.info("Количество найденных вещей равно: {}", items.size());
        log.info("Идентификаторы вещей: \"{}\"",
                items.stream().map(ItemResponseDto::getId).collect(Collectors.toList()));
        return items;
    }

    @PostMapping("/{itemId}/comment")
    @Validated({Marker.OnCreate.class})
    @ResponseStatus(HttpStatus.OK)
    public CommentResponseDto createComment(
            @RequestHeader("X-Sharer-User-Id") Long authorId,
            @PathVariable @NotNull Long itemId,
            @Valid @RequestBody CommentRequestDto newComment) {

        log.info("Запрос на добавление комментария: \"{}\" для вещи с id: {}, пользователем с id: {}",
                newComment, itemId, authorId);
        final CommentResponseDto createdComment = itemService.createComment(authorId, itemId, newComment);
        log.info("Добавлен: {}", createdComment);
        return createdComment;
    }
}
