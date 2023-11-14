package ru.practicum.shareit.request.repository;

import ru.practicum.shareit.request.model.ItemRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
}
