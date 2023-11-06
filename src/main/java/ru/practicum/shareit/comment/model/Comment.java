package ru.practicum.shareit.comment.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                 // уникальный идентификатор комментария
    @Column(nullable = false, length = 1024)
    private String text;             // текст комментария
    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;            // вещь, которую комментируют
    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;          // пользователь - автор комментария
}
