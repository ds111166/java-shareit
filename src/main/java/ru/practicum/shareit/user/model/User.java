package ru.practicum.shareit.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;        // уникальный идентификатор пользователя
    @Column(name = "name", nullable = false)
    private String name;    // имя или логин пользователя
    @Column(nullable = false, unique = true, length = 512)
    private String email;   // адрес электронной почты
}