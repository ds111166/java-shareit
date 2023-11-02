package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                 // уникальный идентификатор вещи
    @Column(nullable = false)
    private String name;             // краткое название
    @Column(nullable = false)
    private String description;      // развёрнутое описание
    @Column(name = "is_available", nullable = false)
    private Boolean available;       // статус о том, доступна или нет вещь для аренды
    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;              // владелец вещи
    @Column(name = "request_id")
    private Long itemRequestId;     //  если вещь была создана по запросу - ссылка на соответствующий запрос
}