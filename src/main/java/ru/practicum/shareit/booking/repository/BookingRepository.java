package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.data.StatusBooking;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByBookerId(Long bookerId, Sort sorting);

    List<Booking> findByBookerIdAndStartLessThanAndEndGreaterThanEqual(Long ownerItemId,
                                                                       LocalDateTime nowDateTime,
                                                                       LocalDateTime nowDateTime1,
                                                                       Sort sorting);

    List<Booking> findByBookerIdAndEndBefore(Long bookerId, LocalDateTime nowDateTime, Sort sorting);

    List<Booking> findByBookerIdAndStartAfter(Long bookerId, LocalDateTime nowDateTime, Sort sorting);

    List<Booking> findByBookerIdAndStatusId(Long bookerId, StatusBooking statusBooking, Sort sorting);

    List<Booking> findByItem_Owner_Id(Long ownerItemId, Sort sorting);

    List<Booking> findByItem_Owner_IdAndStartLessThanAndEndGreaterThanEqual(Long ownerItemId,
                                                                            LocalDateTime nowDateTime,
                                                                            LocalDateTime nowDateTime1,
                                                                            Sort sorting);

    List<Booking> findByItem_Owner_IdAndEndBefore(Long ownerItemId, LocalDateTime nowDateTime, Sort sorting);

    List<Booking> findByItem_Owner_IdAndStartAfter(Long ownerItemId, LocalDateTime nowDateTime, Sort sorting);

    List<Booking> findByItem_Owner_IdAndStatusId(Long ownerItemId, StatusBooking statusBooking, Sort sorting);

    // последнего бронирования start max end после тек даты DESC
    // используется для сортировки возвращаемых данных в порядке убывания
    Booking findFirstByItemIdAndStatusIdAndStartBeforeOrderByEndDesc(Long itemId,
                                                                     StatusBooking statusBooking,
                                                                     LocalDateTime nowDateTime);

    //следующее бронирование Команда ASC команда используется для сортировки возвращаемых данных в порядке возрастания
    Booking findFirstByItemIdAndStatusIdInAndStartAfterOrderByStartAsc(Long itemId,
                                                                       List<StatusBooking> statuses,
                                                                       LocalDateTime nowDateTime);

    boolean existsByItemIdAndBookerIdAndEndIsBeforeAndStatusId(Long itemId,
                                                               Long authorId,
                                                               LocalDateTime now,
                                                               StatusBooking statusBooking);
}
