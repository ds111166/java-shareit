package ru.practicum.shareit.request.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "requests")
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                  // уникальный идентификатор запроса
    @Column(nullable = false, length = 1024)
    private String description;       // текст запроса, содержащий описание требуемой вещи
    @ManyToOne
    @JoinColumn(name = "requestor_id", nullable = false)
    private User requester;           // пользователь, создавший запрос
    @Column(name = "created", nullable = false)
    private LocalDateTime created;    // дата и время создания запроса
}