package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.validation.Marker;


import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Slf4j
@Validated
@RestController
@AllArgsConstructor
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

    @GetMapping("{requestId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemRequestResponseDto getItemRequestById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                     @PathVariable @NotNull Long requestId) {
        log.info("Запрос на получение запроса вещи с id: {} от пользователя с id: {}", requestId, userId);
        ItemRequestResponseDto itemRequestById = itemRequestService.getItemRequestById(userId, requestId);
        log.info("Отправлен: {}", itemRequestById);
        return itemRequestById;
    }

}
