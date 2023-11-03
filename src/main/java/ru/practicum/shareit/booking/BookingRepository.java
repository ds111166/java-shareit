package ru.practicum.shareit.booking;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.data.StatusBooking;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByBookerId(Long bookerId, Sort sorting);

    List<Booking> findByBookerIdAndStartNotAfterAndEndNotBefore(Long bookerId, LocalDateTime nowDateTime,
                                                                LocalDateTime nowDateTime1, Sort sorting);

    List<Booking> findByBookerIdAndEndBefore(Long bookerId, LocalDateTime nowDateTime, Sort sorting);

    List<Booking> findByBookerIdAndStartAfter(Long bookerId, LocalDateTime nowDateTime, Sort sorting);

    List<Booking> findByBookerIdAndStatus(Long bookerId, StatusBooking statusBooking, Sort sorting);

    List<Booking> findByItem_Owner_Id(Long ownerItemId, Sort sorting);

    List<Booking> findByItem_Owner_IdAndStartNotAfterAndEndNotBefore(Long ownerItemId, LocalDateTime nowDateTime, LocalDateTime nowDateTime1, Sort sorting);

    List<Booking> findByItem_Owner_IdAndEndBefore(Long ownerItemId, LocalDateTime nowDateTime, Sort sorting);

    List<Booking> findByItem_Owner_IdAndStartAfter(Long ownerItemId, LocalDateTime nowDateTime, Sort sorting);

    List<Booking> findByItem_Owner_IdAndStatus(Long ownerItemId, StatusBooking statusBooking, Sort sorting);

    // последнего бронирования start max end после тек датф DESC используется для сортировки возвращаемых данных в порядке убывания
    Booking findFirstByItem_IdAndEndAfterOrderByStartDesc(Long itemId, LocalDateTime nowDateTime);

    //следующее бронирование Команда ASC команда используется для сортировки возвращаемых данных в порядке возрастания
    Booking findFirstByItem_IdAndStartAfterOrderByEndAsc(Long itemId, LocalDateTime nowDateTime);

}
