package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.data.StatusBooking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;              // уникальный идентификатор бронирования
    @Column(name = "start_date", nullable = false)
    private LocalDateTime start;  // дата начала бронирования
    @Column(name = "end_date", nullable = false)
    private LocalDateTime end;    // дата конца бронирования
    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;            // вещь, которую пользователь бронирует
    @ManyToOne
    @JoinColumn(name = "booker_id", nullable = false)
    private User booker;          // пользователь, который осуществляет бронирование
    @Enumerated(EnumType.ORDINAL)
    private StatusBooking statusId; // статус бронирования
}
