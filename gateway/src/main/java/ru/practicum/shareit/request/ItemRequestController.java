package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.validation.Marker;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    @Validated({Marker.OnCreate.class})
    public ResponseEntity<Object> createItemRequest(
            @RequestHeader("X-Sharer-User-Id") Long requestorId,
            @Valid @RequestBody ItemRequestCreateDto newItemRequest) {
        log.info("Create item request {}, requestorId={}", newItemRequest, requestorId);
        return itemRequestClient.createItemRequest(requestorId, newItemRequest);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequestById(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable @NotNull Long requestId) {
        log.info("Get itemRequest by id={}, userId={}", requestId, userId);
        return itemRequestClient.getItemRequestById(userId, requestId);
    }

    @GetMapping
    public ResponseEntity<Object> getItemRequests(@RequestHeader("X-Sharer-User-Id") Long requestorId) {
        log.info("Get itemRequest by requestor id={}", requestorId);
        return itemRequestClient.getItemRequests(requestorId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getItemRequestsAll(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @Min(value = 0, message = "Индекс первого элемента не должен быть меньше нуля!")
            @RequestParam(value = "from", defaultValue = "0") Integer from,
            @Min(value = 1, message = "Количество элементов для отображения не должно быть меньше единицы!")
            @RequestParam(value = "size", required = false) Integer size) {
        log.info("Get itemRequests all userId={}, from={}, size={}", userId, from, size);
        return itemRequestClient.getItemRequestsAll(userId, from, size);
    }

}
