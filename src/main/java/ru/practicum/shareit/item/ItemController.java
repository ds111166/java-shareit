package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.validation.Marker;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDto> getOwnerItems(@RequestHeader("X-Sharer-User-Id") Long ownerId) {
        log.info("Запрос на получение списка вещей владельца с id: {}", ownerId);
        final List<ItemDto> items = itemService.getOwnerItems(ownerId);
        log.info("Количество найденных вещей владельца с id: {} равно: {}", ownerId, items.size());
        return items;
    }

    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto getItemById(@RequestHeader("X-Sharer-User-Id") Long userId,
                               @PathVariable @NotNull Long itemId) {
        log.info("Запрос на получение вещи с id: {}", itemId);
        final ItemDto itemById = itemService.getItemById(userId, itemId);
        log.info("Отправлена: {}", itemById);
        return itemById;
    }

    @PostMapping
    @Validated({Marker.OnCreate.class})
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                              @Valid @RequestBody ItemDto newItem) {
        log.info("Запрос на добавление: {} владельцем с id: {}", newItem, ownerId);
        final ItemDto createdItem = itemService.createItem(ownerId, newItem);
        log.info("Добавлена: {}", createdItem);
        return createdItem;
    }

    @PatchMapping("/{itemId}")
    @Validated({Marker.OnUpdate.class})
    @ResponseStatus(HttpStatus.OK)
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                              @PathVariable @NotNull Long itemId,
                              @Valid @RequestBody ItemDto itemData) {
        log.info("Запрос на обновление: {}, id вещи: {}, id владельца: {}", itemData, itemId, ownerId);
        final ItemDto updatedItem = itemService.updateItem(ownerId, itemId, itemData);
        log.info("Обновлена: {}", updatedItem);
        return updatedItem;
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDto> searchItemsByText(@RequestParam("text") String text) {
        log.info("Запрос поиска вещей по описанию: \"{}\"", text);
        final List<ItemDto> items = itemService.searchItemsByText(text);
        log.info("Количество найденных вещей равно: {}", items.size());
        return items;
    }

    @PostMapping("/{itemId}/comment")
    @Validated({Marker.OnCreate.class})
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createComment(@RequestHeader("X-Sharer-User-Id") Long authorId,
                                    @PathVariable @NotNull Long itemId,
                                    @Valid @RequestBody CommentDto newComment) {
        log.info("Запрос на добавление комментария: \"{}\" для вещи с id: {}, пользователем с id: {}",
                newComment, itemId, authorId);
        final CommentDto createdComment = itemService.createComment(authorId, itemId, newComment);
        log.info("Добавлен: {}", createdComment);
        return createdComment;
    }

}
