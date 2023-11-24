package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.data.StatusBooking;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingServiceImplTest {

    private final EntityManager em;
    private final ItemService itemService;
    private final UserService userService;
    private final BookingService bookingService;
    private final BookingMapper bookingMapper;
    private final UserMapper userMapper;
    private final ItemMapper itemMapper;

    @Test
    void createBooking() {
        final LocalDateTime now = LocalDateTime.now();
        ItemCreateDto itemCreateDto1 = new ItemCreateDto("item1", "des_item1",
                true, null);
        ItemCreateDto itemCreateDto2 = new ItemCreateDto("item2", "des_item2",
                false, null);
        UserRequestDto ownerRequestDto1 = new UserRequestDto("owner1", "owner1@mail.ru");
        UserRequestDto bookerRequestDto = new UserRequestDto("booker", "booker@mail.ru");
        final UserResponseDto owner1 = userService.createUser(ownerRequestDto1);
        final UserResponseDto booker = userService.createUser(bookerRequestDto);
        final ItemResponseDto item1 = itemService.createItem(owner1.getId(), itemCreateDto1);
        final ItemResponseDto item2 = itemService.createItem(owner1.getId(), itemCreateDto2);

        final BookingRequestDto bookingRequestDto1 = new BookingRequestDto(item1.getId(),
                now.plusDays(1L), now.plusDays(2L));
        assertThrows(NotFoundException.class, () -> bookingService
                .createBooking(owner1.getId(), bookingRequestDto1));

        final BookingDto createdBooking1 = bookingService.createBooking(booker.getId(), bookingRequestDto1);
        final TypedQuery<Booking> query = em.createQuery("Select b from  Booking b where b.id = :id", Booking.class);
        final Booking booking = query.setParameter("id", createdBooking1.getId()).getSingleResult();
        final BookingDto bookingDto = bookingMapper.toBookingDto(booking, item1, booker);
        assertThat(bookingDto).isEqualTo(createdBooking1);

        final BookingRequestDto bookingRequestDto2 = new BookingRequestDto(item2.getId(),
                now.plusDays(1L), now.plusDays(2L));
        assertThrows(ValidationException.class, () -> bookingService
                .createBooking(booker.getId(), bookingRequestDto2));

    }

    @Test
    void getBookingById() {
        final LocalDateTime now = LocalDateTime.now();
        ItemCreateDto itemCreateDto1 = new ItemCreateDto("item", "des_item",
                true, null);
        UserRequestDto ownerRequestDto1 = new UserRequestDto("owner1", "owner1@mail.ru");
        UserRequestDto bookerRequestDto = new UserRequestDto("booker", "booker@mail.ru");
        UserRequestDto userRequestDto = new UserRequestDto("user", "user@mail.ru");
        final UserResponseDto owner1 = userService.createUser(ownerRequestDto1);
        final UserResponseDto booker = userService.createUser(bookerRequestDto);
        final UserResponseDto user = userService.createUser(userRequestDto);
        final ItemResponseDto item1 = itemService.createItem(owner1.getId(), itemCreateDto1);
        final BookingRequestDto bookingRequestDto = new BookingRequestDto(item1.getId(),
                now.plusDays(1L), now.plusDays(2L));
        final BookingDto createdBooking = bookingService.createBooking(booker.getId(), bookingRequestDto);
        final BookingDto bookingById = bookingService.getBookingById(booker.getId(), createdBooking.getId());
        assertThat(bookingById).isEqualTo(createdBooking);

        assertThrows(NotFoundException.class, () -> bookingService
                .getBookingById(user.getId(), createdBooking.getId()));
    }

    @Test
    void approvalBooking() {
        final LocalDateTime now = LocalDateTime.now();
        ItemCreateDto itemCreateDto1 = new ItemCreateDto("item", "des_item",
                true, null);
        UserRequestDto ownerRequestDto1 = new UserRequestDto("owner1", "owner1@mail.ru");
        UserRequestDto bookerRequestDto = new UserRequestDto("booker", "booker@mail.ru");
        final UserResponseDto owner1 = userService.createUser(ownerRequestDto1);
        final UserResponseDto booker = userService.createUser(bookerRequestDto);
        final ItemResponseDto item1 = itemService.createItem(owner1.getId(), itemCreateDto1);
        final BookingRequestDto bookingRequestDto1 = new BookingRequestDto(item1.getId(),
                now.plusDays(1L), now.plusDays(2L));

        final BookingDto createdBooking1 = bookingService.createBooking(booker.getId(), bookingRequestDto1);
        assertThat(createdBooking1.getStatus()).isEqualTo(StatusBooking.WAITING);

        final BookingDto bookingDto1 = bookingService.approvalBooking(owner1.getId(), createdBooking1.getId(),
                false);
        assertThat(bookingDto1.getStatus()).isEqualTo(StatusBooking.REJECTED);

        final BookingRequestDto bookingRequestDto2 = new BookingRequestDto(item1.getId(),
                now.plusDays(2L), now.plusDays(13L));

        final BookingDto createdBooking2 = bookingService.createBooking(booker.getId(), bookingRequestDto2);
        assertThat(createdBooking1.getStatus()).isEqualTo(StatusBooking.WAITING);

        final BookingDto bookingDto2 = bookingService.approvalBooking(owner1.getId(), createdBooking2.getId(),
                true);
        assertThat(bookingDto2.getStatus()).isEqualTo(StatusBooking.APPROVED);
    }

    @Test
    void getBookings() {
        final LocalDateTime now = LocalDateTime.now();
        ItemCreateDto itemCreateDto1 = new ItemCreateDto("item", "des_item",
                true, null);
        UserRequestDto ownerRequestDto1 = new UserRequestDto("owner1", "owner1@mail.ru");
        UserRequestDto bookerRequestDto = new UserRequestDto("booker", "booker@mail.ru");
        final UserResponseDto ownerDto1 = userService.createUser(ownerRequestDto1);
        final UserResponseDto bookerDto = userService.createUser(bookerRequestDto);
        final ItemResponseDto itemDto1 = itemService.createItem(ownerDto1.getId(), itemCreateDto1);
        final BookingRequestDto bookingRequestDto1 = new BookingRequestDto(itemDto1.getId(),
                now.minusDays(11L), now.minusDays(5L));
        final BookingRequestDto bookingRequestDto2 = new BookingRequestDto(itemDto1.getId(),
                now.minusDays(1L), now.plusDays(2L));
        final BookingRequestDto bookingRequestDto3 = new BookingRequestDto(itemDto1.getId(),
                now.plusDays(1L), now.plusDays(4L));
        final BookingRequestDto bookingRequestDto4 = new BookingRequestDto(itemDto1.getId(),
                now.plusDays(3L), now.plusDays(6L));

        final User owner1 = userMapper.toUser(ownerDto1);
        final User booker = userMapper.toUser(bookerDto);

        Booking booking1 = bookingMapper.toBooking(bookingRequestDto1,
                itemMapper.toItem(itemDto1, owner1), booker, StatusBooking.APPROVED);
        Booking booking2 = bookingMapper.toBooking(bookingRequestDto2,
                itemMapper.toItem(itemDto1, owner1), booker, StatusBooking.APPROVED);
        em.persist(booking1);
        em.persist(booking2);
        em.flush();
        BookingDto createdBooking1 = bookingService.getBookingById(ownerDto1.getId(), booking1.getId());
        BookingDto createdBooking2 = bookingService.getBookingById(ownerDto1.getId(), booking2.getId());

        BookingDto createdBooking3 = bookingService.createBooking(bookerDto.getId(), bookingRequestDto3);
        BookingDto createdBooking4 = bookingService.createBooking(bookerDto.getId(), bookingRequestDto4);

        createdBooking3 = bookingService.approvalBooking(ownerDto1.getId(), createdBooking3.getId(), false);

        assertThrows(ValidationException.class, () -> bookingService
                .getBookings(bookerDto.getId(), "qwerty", 0, 2222));
        final List<BookingDto> bookingsAll = bookingService
                .getBookings(bookerDto.getId(), "ALL", 0, 10);
        assertThat(bookingsAll).hasSize(4)
                .containsAll(List.of(createdBooking1, createdBooking2, createdBooking3, createdBooking4));
        final List<BookingDto> bookingsCurrent = bookingService
                .getBookings(bookerDto.getId(), "CURRENT", 0, 10);
        assertThat(bookingsCurrent).hasSize(1)
                .containsAll(List.of(createdBooking2));
        final List<BookingDto> bookingsPast = bookingService
                .getBookings(bookerDto.getId(), "PAST", 0, 10);
        assertThat(bookingsPast).hasSize(1)
                .containsAll(List.of(createdBooking1));
        final List<BookingDto> bookingsFuture = bookingService
                .getBookings(bookerDto.getId(), "FUTURE", 0, 10);
        assertThat(bookingsFuture).hasSize(2)
                .containsAll(List.of(createdBooking3, createdBooking4));
        final List<BookingDto> bookingsWaiting = bookingService
                .getBookings(bookerDto.getId(), "WAITING", 0, 10);
        assertThat(bookingsWaiting).hasSize(1)
                .containsAll(List.of(createdBooking4));
        final List<BookingDto> bookingsReject = bookingService
                .getBookings(bookerDto.getId(), "REJECTED", 0, 10);
        assertThat(bookingsReject).hasSize(1)
                .containsAll(List.of(createdBooking3));
    }

    @Test
    void getBookingsByOwnerItemId() {
        final LocalDateTime now = LocalDateTime.now();
        ItemCreateDto itemCreateDto1 = new ItemCreateDto("item", "des_item",
                true, null);
        UserRequestDto ownerRequestDto1 = new UserRequestDto("owner1", "owner1@mail.ru");
        UserRequestDto bookerRequestDto = new UserRequestDto("booker", "booker@mail.ru");
        final UserResponseDto ownerDto1 = userService.createUser(ownerRequestDto1);
        final UserResponseDto bookerDto = userService.createUser(bookerRequestDto);
        final ItemResponseDto itemDto1 = itemService.createItem(ownerDto1.getId(), itemCreateDto1);
        final BookingRequestDto bookingRequestDto1 = new BookingRequestDto(itemDto1.getId(),
                now.minusDays(11L), now.minusDays(5L));
        final BookingRequestDto bookingRequestDto2 = new BookingRequestDto(itemDto1.getId(),
                now.minusDays(1L), now.plusDays(2L));
        final BookingRequestDto bookingRequestDto3 = new BookingRequestDto(itemDto1.getId(),
                now.plusDays(1L), now.plusDays(4L));
        final BookingRequestDto bookingRequestDto4 = new BookingRequestDto(itemDto1.getId(),
                now.plusDays(3L), now.plusDays(6L));

        final User owner1 = userMapper.toUser(ownerDto1);
        final User booker = userMapper.toUser(bookerDto);

        Booking booking1 = bookingMapper.toBooking(bookingRequestDto1,
                itemMapper.toItem(itemDto1, owner1), booker, StatusBooking.APPROVED);
        Booking booking2 = bookingMapper.toBooking(bookingRequestDto2,
                itemMapper.toItem(itemDto1, owner1), booker, StatusBooking.APPROVED);
        em.persist(booking1);
        em.persist(booking2);
        em.flush();
        BookingDto createdBooking1 = bookingService.getBookingById(ownerDto1.getId(), booking1.getId());
        BookingDto createdBooking2 = bookingService.getBookingById(ownerDto1.getId(), booking2.getId());

        BookingDto createdBooking3 = bookingService.createBooking(bookerDto.getId(), bookingRequestDto3);
        BookingDto createdBooking4 = bookingService.createBooking(bookerDto.getId(), bookingRequestDto4);

        createdBooking3 = bookingService.approvalBooking(ownerDto1.getId(), createdBooking3.getId(), false);

        assertThrows(ValidationException.class, () -> bookingService
                .getBookingsByOwnerItemId(ownerDto1.getId(), "qwerty", 0, 2222));
        final List<BookingDto> bookingsAll = bookingService
                .getBookingsByOwnerItemId(ownerDto1.getId(), "ALL", 0, 10);
        assertThat(bookingsAll).hasSize(4)
                .containsAll(List.of(createdBooking1, createdBooking2, createdBooking3, createdBooking4));
        final List<BookingDto> bookingsCurrent = bookingService
                .getBookingsByOwnerItemId(ownerDto1.getId(), "CURRENT", 0, 10);
        assertThat(bookingsCurrent).hasSize(1)
                .containsAll(List.of(createdBooking2));
        final List<BookingDto> bookingsPast = bookingService
                .getBookingsByOwnerItemId(ownerDto1.getId(), "PAST", 0, 10);
        assertThat(bookingsPast).hasSize(1)
                .containsAll(List.of(createdBooking1));
        final List<BookingDto> bookingsFuture = bookingService
                .getBookingsByOwnerItemId(ownerDto1.getId(), "FUTURE", 0, 10);
        assertThat(bookingsFuture).hasSize(2)
                .containsAll(List.of(createdBooking3, createdBooking4));
        final List<BookingDto> bookingsWaiting = bookingService
                .getBookingsByOwnerItemId(ownerDto1.getId(), "WAITING", 0, 10);
        assertThat(bookingsWaiting).hasSize(1)
                .containsAll(List.of(createdBooking4));
        final List<BookingDto> bookingsReject = bookingService
                .getBookingsByOwnerItemId(ownerDto1.getId(), "REJECTED", 0, 10);
        assertThat(bookingsReject).hasSize(1)
                .containsAll(List.of(createdBooking3));
    }
}