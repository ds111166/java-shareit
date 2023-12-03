package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.service.ItemRequestService;
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
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    @Validated({Marker.OnCreate.class})
    @ResponseStatus(HttpStatus.CREATED)
    public ItemRequestResponseDto createItemRequest(@RequestHeader("X-Sharer-User-Id") Long requestorId,
                                                    @Valid @RequestBody ItemRequestCreateDto newItemRequest) {
        log.info("Получен запрос, содержащий описание требуемой вещи: \"{}\" от пользователя с id: {}",
                newItemRequest, requestorId);
        ItemRequestResponseDto createdItemRequest = itemRequestService.createItemRequest(requestorId, newItemRequest);
        log.info("Добавлен: {}", createdItemRequest);

        return createdItemRequest;
    }

    @GetMapping("/{requestId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemRequestResponseDto getItemRequestById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                     @PathVariable @NotNull Long requestId) {
        log.info("Запрос на получение запроса вещи с id: {} от пользователя с id: {}", requestId, userId);
        ItemRequestResponseDto itemRequestById = itemRequestService.getItemRequestById(userId, requestId);
        log.info("Отправлен: {}", itemRequestById);
        return itemRequestById;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemRequestResponseDto> getItemRequests(@RequestHeader("X-Sharer-User-Id") Long requestorId) {
        log.info("Запрос на получение запросов вещей от пользователя с id: {}", requestorId);
        List<ItemRequestResponseDto> itemRequests = itemRequestService.getItemRequests(requestorId);
        log.info("\"Количество найденных запросов вещей от пользователя с id: {} равно: {}\"",
                requestorId, itemRequests.size());
        log.info("Идентификаторы запросов вещей: \"{}\"",
                itemRequests.stream().map(ItemRequestResponseDto::getId).collect(Collectors.toList()));
        return itemRequests;
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemRequestResponseDto> getItemRequestsAll(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @Min(value = 0, message = "Индекс первого элемента не должен быть меньше нуля!")
            @RequestParam(value = "from", defaultValue = "0") Integer from,
            @Min(value = 1, message = "Количество элементов для отображения не должно быть меньше единицы!")
            @RequestParam(value = "size", required = false) Integer size) {
        log.info("Запрос от пользователя с id: {} на получение запросов вещей от других пользователей,\n " +
                        "по странично, начиная с позиции: {}, величина страницы: {} ",
                userId, from, (size == null) ? "\"не определена\"" : size);
        List<ItemRequestResponseDto> itemRequests = itemRequestService.getItemRequestsAll(userId,
                from, (size == null) ? Integer.MAX_VALUE : size);
        log.info("\"Количество найденных запросов вещей от пользователей равно: {}\"",
                itemRequests.size());
        log.info("Идентификаторы запросов вещей: \"{}\"",
                itemRequests.stream().map(ItemRequestResponseDto::getId).collect(Collectors.toList()));
        return itemRequests;
    }

}
