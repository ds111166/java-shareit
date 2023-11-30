package ru.practicum.shareit.booking.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.booking.data.StatusBooking;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class BookingRepositoryTest {
    private final TestEntityManager em;
    private final BookingRepository bookingRepository;

    private User user1 = null;
    private User user2 = null;
    private User user3 = null;
    private User user4 = null;

    private Item item1 = null;
    private Item item2 = null;
    private Item item3 = null;
    private Item item4 = null;
    private Item item5 = null;
    private Item item6 = null;

    @BeforeEach
    void setUp() {
        user1 = (user1 == null) ? em.persist(new User(null, "user1", "user1@mail.ru")) : user1;
        user2 = (user2 == null) ? em.persist(new User(null, "user2", "user2@mail.ru")) : user2;
        user3 = (user3 == null) ? em.persist(new User(null, "user3", "user3@mail.ru")) : user3;
        user4 = (user4 == null) ? em.persist(new User(null, "user4", "user4@mail.ru")) : user4;
        item1 = (item1 == null) ? em.persist(new Item(null, "Перфоратор", "Перфоратор-дрель",
                true, user1, null)) : item1;
        item2 = (item2 == null) ? em.persist(new Item(null, "Нож", "нож кухонный",
                true, user1, null)) : item2;
        item3 = (item3 == null) ? em.persist(new Item(null, "лом", "лом стальной",
                true, user1, null)) : item3;
        item4 = (item4 == null) ? em.persist(new Item(null, "проектор", "проектор лазерный",
                true, user2, null)) : item4;
        item5 = (item5 == null) ? em.persist(new Item(null, "дрель", "дрель, шуруповерт",
                true, user2, null)) : item5;
        item6 = (item6 == null) ? em.persist(new Item(null, "сверла", "сверла по металлу",
                true, user3, null)) : item6;
    }

    @Test
    public void contextLoads() {
        Assertions.assertNotNull(em);
    }

    @Test
    public void shouldFindNoUsersIfRepositoryIsEmpty() {
        final List<Booking> bookings = bookingRepository.findAll();
        assertThat(bookings).isEmpty();
    }

    @Test
    void findByBookerId() {
        final LocalDateTime now = LocalDateTime.now();
        Booking booking1 = em.persist(new Booking(null, now.plusDays(1L), now.plusDays(2L), item1,
                user4, StatusBooking.APPROVED));
        Booking booking2 = em.persist(new Booking(null, now.plusDays(1L), now.plusDays(2L), item2,
                user4, StatusBooking.WAITING));
        Booking booking3 = em.persist(new Booking(null, now.plusDays(1L), now.plusDays(2L), item3,
                user4, StatusBooking.WAITING));
        final List<Booking> byBookerId2 = bookingRepository.findByBookerId(user2.getId(), null);
        assertThat(byBookerId2).isEmpty();
        final List<Booking> byBookerId4 = bookingRepository.findByBookerId(user4.getId(), null);
        assertThat(byBookerId4).hasSize(3).contains(booking1, booking2, booking3);
    }

    @Test
    void findByBookerIdAndStartLessThanAndEndGreaterThanEqual() {
        final LocalDateTime now = LocalDateTime.now();
        Booking booking1 = em.persist(new Booking(null, now.plusDays(1L), now.plusDays(2L),
                item1, user4, StatusBooking.WAITING));
        Booking booking2 = em.persist(new Booking(null, now.plusDays(1L), now.plusDays(2L),
                item2, user4, StatusBooking.WAITING));
        Booking booking3 = em.persist(new Booking(null, now.plusDays(1L), now.plusDays(3L),
                item3, user4, StatusBooking.WAITING));
        final List<Booking> byBookerIdAndStartLessThanAndEndGreaterThanEqual4 = bookingRepository
                .findByBookerIdAndStartLessThanAndEndGreaterThanEqual(user4.getId(), now.plusDays(1L).plusMinutes(2L),
                        now.plusDays(2L).plusHours(12L), null);
        assertThat(byBookerIdAndStartLessThanAndEndGreaterThanEqual4).hasSize(1).contains(booking3);
        final List<Booking> byBookerIdAndStartLessThanAndEndGreaterThanEqual1 = bookingRepository
                .findByBookerIdAndStartLessThanAndEndGreaterThanEqual(user1.getId(), now.plusDays(1L).plusMinutes(2L),
                        now.plusDays(2L).plusHours(12L), null);
        assertThat(byBookerIdAndStartLessThanAndEndGreaterThanEqual1).isEmpty();

        final List<Booking> byBookerIdAndStartLessThanAndEndGreaterThanEqual = bookingRepository
                .findByBookerIdAndStartLessThanAndEndGreaterThanEqual(user4.getId(), now.plusDays(1L).plusMinutes(2L),
                        now.plusDays(1L), null);
        assertThat(byBookerIdAndStartLessThanAndEndGreaterThanEqual)
                .hasSize(3).contains(booking1, booking2, booking3);

    }

    @Test
    void findByBookerIdAndEndBefore() {
        final LocalDateTime now = LocalDateTime.now();
        Booking booking1 = em.persist(new Booking(null, now.plusDays(1L), now.plusDays(2L),
                item1, user4, StatusBooking.WAITING));
        Booking booking2 = em.persist(new Booking(null, now.plusDays(1L), now.plusDays(2L),
                item2, user4, StatusBooking.WAITING));
        Booking booking3 = em.persist(new Booking(null, now.plusDays(1L), now.plusDays(3L),
                item3, user4, StatusBooking.WAITING));
        final List<Booking> byBookerIdAndEndBefore4 = bookingRepository
                .findByBookerIdAndEndBefore(user4.getId(), now.plusDays(2L).plusHours(12L), null);
        assertThat(byBookerIdAndEndBefore4).hasSize(2).contains(booking1, booking2);
        assertThat(byBookerIdAndEndBefore4).doesNotContainAnyElementsOf(List.of(booking3));
        final List<Booking> byBookerIdAndEndBefore1 = bookingRepository
                .findByBookerIdAndEndBefore(user1.getId(), now.plusDays(2L).plusHours(12L), null);
        assertThat(byBookerIdAndEndBefore1).isEmpty();
    }

    @Test
    void findByBookerIdAndStartAfter() {
        final LocalDateTime now = LocalDateTime.now();
        Booking booking1 = em.persist(new Booking(null, now.plusDays(1L), now.plusDays(2L),
                item1, user4, StatusBooking.WAITING));
        Booking booking2 = em.persist(new Booking(null, now.plusDays(1L), now.plusDays(2L),
                item2, user4, StatusBooking.WAITING));
        Booking booking3 = em.persist(new Booking(null, now.plusHours(1L), now.plusDays(3L),
                item3, user4, StatusBooking.WAITING));
        final List<Booking> byBookerIdAndStartAfter4 = bookingRepository
                .findByBookerIdAndStartAfter(user4.getId(), now.plusHours(12L), null);
        assertThat(byBookerIdAndStartAfter4).hasSize(2).contains(booking1, booking2);
        assertThat(byBookerIdAndStartAfter4).doesNotContainAnyElementsOf(List.of(booking3));
        final List<Booking> byBookerIdAndStartAfter1 = bookingRepository
                .findByBookerIdAndStartAfter(user1.getId(), now.plusHours(12L), null);
        assertThat(byBookerIdAndStartAfter1).isEmpty();
    }

    @Test
    void findByBookerIdAndStatusId() {
        final LocalDateTime now = LocalDateTime.now();
        Booking booking1 = em.persist(new Booking(null, now.plusDays(1L), now.plusDays(2L), item1,
                user4, StatusBooking.APPROVED));
        Booking booking2 = em.persist(new Booking(null, now.plusDays(1L), now.plusDays(2L), item2,
                user4, StatusBooking.WAITING));
        Booking booking3 = em.persist(new Booking(null, now.plusDays(1L), now.plusDays(2L), item3,
                user4, StatusBooking.WAITING));
        final List<Booking> byBookerIdAndStatusId2 = bookingRepository.findByBookerIdAndStatusId(user2.getId(),
                StatusBooking.WAITING, null);
        assertThat(byBookerIdAndStatusId2).isEmpty();
        final List<Booking> byBookerIdAndStatusId4 = bookingRepository.findByBookerIdAndStatusId(user4.getId(),
                StatusBooking.WAITING, null);
        assertThat(byBookerIdAndStatusId4).hasSize(2)
                .contains(booking2, booking3)
                .doesNotContainAnyElementsOf(List.of(booking1));

    }

    @Test
    void findByItem_Owner_Id() {
        final LocalDateTime now = LocalDateTime.now();
        Booking booking1 = em.persist(new Booking(null, now.plusDays(1L), now.plusDays(2L), item1,
                user4, StatusBooking.APPROVED));
        Booking booking2 = em.persist(new Booking(null, now.plusDays(1L), now.plusDays(2L), item2,
                user4, StatusBooking.WAITING));
        Booking booking3 = em.persist(new Booking(null, now.plusDays(1L), now.plusDays(2L), item3,
                user4, StatusBooking.WAITING));
        final List<Booking> byItemOwnerId2 = bookingRepository.findByItem_Owner_Id(user2.getId(), null);
        assertThat(byItemOwnerId2).isEmpty();
        final List<Booking> byItemOwnerId1 = bookingRepository.findByItem_Owner_Id(user1.getId(), null);
        assertThat(byItemOwnerId1).hasSize(3).contains(booking1, booking2, booking3);
    }

    @Test
    void findByItem_Owner_IdAndStartLessThanAndEndGreaterThanEqual() {
        final LocalDateTime now = LocalDateTime.now();
        Booking booking1 = em.persist(new Booking(null, now.plusDays(1L), now.plusDays(2L),
                item1, user4, StatusBooking.WAITING));
        Booking booking2 = em.persist(new Booking(null, now.plusDays(1L), now.plusDays(2L),
                item2, user4, StatusBooking.WAITING));
        Booking booking3 = em.persist(new Booking(null, now.plusDays(1L), now.plusDays(3L),
                item3, user4, StatusBooking.WAITING));
        final List<Booking> byItemOwnerIdAndStartLessThanAndEndGreaterThanEqual1 = bookingRepository
                .findByItem_Owner_IdAndStartLessThanAndEndGreaterThanEqual(user1.getId(),
                        now.plusDays(1L).plusMinutes(2L),
                        now.plusDays(2L).plusHours(12L),
                        null);
        assertThat(byItemOwnerIdAndStartLessThanAndEndGreaterThanEqual1).hasSize(1).contains(booking3);

        final List<Booking> byItemOwnerIdAndStartLessThanAndEndGreaterThanEqual2 = bookingRepository
                .findByItem_Owner_IdAndStartLessThanAndEndGreaterThanEqual(user1.getId(),
                        now.plusDays(1L).plusMinutes(2L),
                        now.plusDays(1L).plusHours(12L),
                        null);
        assertThat(byItemOwnerIdAndStartLessThanAndEndGreaterThanEqual2).hasSize(3)
                .containsAll(List.of(booking1, booking2, booking3));

        final List<Booking> byItemOwnerIdAndStartLessThanAndEndGreaterThanEqual4 = bookingRepository
                .findByItem_Owner_IdAndStartLessThanAndEndGreaterThanEqual(user4.getId(),
                        now.plusDays(1L).plusMinutes(2L),
                        now.plusDays(2L).plusHours(12L),
                        null);
        assertThat(byItemOwnerIdAndStartLessThanAndEndGreaterThanEqual4).isEmpty();
    }

    @Test
    void findByItem_Owner_IdAndEndBefore() {
        final LocalDateTime now = LocalDateTime.now();
        Booking booking1 = em.persist(new Booking(null, now.plusDays(1L), now.plusDays(2L),
                item1, user4, StatusBooking.WAITING));
        Booking booking2 = em.persist(new Booking(null, now.plusDays(1L), now.plusDays(2L),
                item2, user4, StatusBooking.WAITING));
        Booking booking3 = em.persist(new Booking(null, now.plusDays(1L), now.plusDays(3L),
                item3, user4, StatusBooking.WAITING));
        final List<Booking> byItemOwnerIdAndEndBefore1 = bookingRepository
                .findByItem_Owner_IdAndEndBefore(user1.getId(), now.plusDays(2L).plusHours(12L), null);
        assertThat(byItemOwnerIdAndEndBefore1).hasSize(2).contains(booking1, booking2);

        final List<Booking> byItemOwnerIdAndEndBefore4 = bookingRepository
                .findByItem_Owner_IdAndEndBefore(user4.getId(), now.plusDays(2L).plusHours(12L), null);
        assertThat(byItemOwnerIdAndEndBefore4).isEmpty();

        final List<Booking> byItemOwnerIdAndEndBefore2 = bookingRepository
                .findByItem_Owner_IdAndEndBefore(user1.getId(), now.plusDays(3L).plusHours(12L), null);
        assertThat(byItemOwnerIdAndEndBefore2).hasSize(3).contains(booking1, booking2, booking3);
    }

    @Test
    void findByItem_Owner_IdAndStartAfter() {
        final LocalDateTime now = LocalDateTime.now();
        Booking booking1 = em.persist(new Booking(null, now.plusDays(1L), now.plusDays(2L),
                item1, user4, StatusBooking.WAITING));
        Booking booking2 = em.persist(new Booking(null, now.plusDays(1L), now.plusDays(2L),
                item2, user4, StatusBooking.WAITING));
        em.persist(new Booking(null, now.plusHours(1L), now.plusDays(3L),
                item3, user4, StatusBooking.WAITING));
        final List<Booking> byItemOwnerIdAndStartAfter1 = bookingRepository
                .findByItem_Owner_IdAndStartAfter(user1.getId(), now.plusHours(12L), null);
        assertThat(byItemOwnerIdAndStartAfter1).hasSize(2).contains(booking1, booking2);
        final List<Booking> byItemOwnerIdAndStartAfter4 = bookingRepository
                .findByItem_Owner_IdAndStartAfter(user4.getId(), now.plusHours(12L), null);
        assertThat(byItemOwnerIdAndStartAfter4).isEmpty();
    }

    @Test
    void findByItem_Owner_IdAndStatusId() {
        final LocalDateTime now = LocalDateTime.now();
        em.persist(new Booking(null, now.plusDays(1L), now.plusDays(2L),
                item1, user4, StatusBooking.APPROVED));
        Booking booking2 = em.persist(new Booking(null, now.plusDays(1L), now.plusDays(2L),
                item2, user4, StatusBooking.WAITING));
        Booking booking3 = em.persist(new Booking(null, now.plusDays(1L), now.plusDays(2L),
                item3, user4, StatusBooking.WAITING));
        final List<Booking> byItemOwnerIdAndStatusId2 = bookingRepository.findByItem_Owner_IdAndStatusId(user2.getId(),
                StatusBooking.WAITING, null);
        assertThat(byItemOwnerIdAndStatusId2).isEmpty();
        final List<Booking> byItemOwnerIdAndStatusId1 = bookingRepository.findByItem_Owner_IdAndStatusId(user1.getId(),
                StatusBooking.WAITING, null);
        assertThat(byItemOwnerIdAndStatusId1).hasSize(2).contains(booking2, booking3);
    }

    @Test
    void findFirstByItemIdAndStatusIdAndStartBeforeOrderByEndDesc() {
        final LocalDateTime now = LocalDateTime.now();
        Booking booking1 = em.persist(new Booking(null, now.plusDays(1L), now.plusDays(2L),
                item1, user4, StatusBooking.APPROVED));
        em.persist(new Booking(null, now.plusDays(1L), now.plusDays(2L),
                item2, user4, StatusBooking.WAITING));
        em.persist(new Booking(null, now.plusDays(1L), now.plusDays(2L),
                item3, user4, StatusBooking.WAITING));
        final Booking booking = bookingRepository
                .findFirstByItemIdAndStatusIdAndStartBeforeOrderByEndDesc(item1.getId(), StatusBooking.APPROVED,
                        now.plusDays(1L).plusMinutes(2L));
        assertThat(booking).isEqualTo(booking1);
        final Booking bookingNull = bookingRepository
                .findFirstByItemIdAndStatusIdAndStartBeforeOrderByEndDesc(item1.getId(), StatusBooking.APPROVED,
                        now.plusDays(1L));
        assertThat(bookingNull).isNull();
    }

    @Test
    void findFirstByItemIdAndStatusIdInAndStartAfterOrderByStartAsc() {
        final LocalDateTime now = LocalDateTime.now();
        Booking booking1 = em.persist(new Booking(null, now.plusDays(1L).plusMinutes(1L), now.plusDays(2L),
                item1, user4, StatusBooking.APPROVED));
        em.persist(new Booking(null, now.plusDays(1L).plusMinutes(2L), now.plusDays(2L),
                item1, user3, StatusBooking.APPROVED));
        em.persist(new Booking(null, now.plusDays(1L), now.plusDays(2L),
                item3, user4, StatusBooking.WAITING));
        final Booking booking = bookingRepository
                .findFirstByItemIdAndStatusIdInAndStartAfterOrderByStartAsc(item1.getId(),
                        List.of(StatusBooking.APPROVED), now);
        assertThat(booking).isEqualTo(booking1);
        final Booking bookingNull = bookingRepository
                .findFirstByItemIdAndStatusIdInAndStartAfterOrderByStartAsc(item3.getId(),
                        List.of(StatusBooking.APPROVED), now);
        assertThat(bookingNull).isNull();
    }

    @Test
    void existsByItemIdAndBookerIdAndEndIsBeforeAndStatusId() {
        final LocalDateTime now = LocalDateTime.now();
        em.persist(new Booking(null, now.plusDays(1L).plusMinutes(1L), now.plusDays(2L),
                item1, user4, StatusBooking.APPROVED));
        em.persist(new Booking(null, now.plusDays(1L).plusMinutes(2L), now.plusDays(2L),
                item1, user3, StatusBooking.APPROVED));
        em.persist(new Booking(null, now.plusDays(1L), now.plusDays(2L), item3,
                user4, StatusBooking.WAITING));
        final boolean isExists = bookingRepository
                .existsByItemIdAndBookerIdAndEndIsBeforeAndStatusId(item1.getId(), user4.getId(),
                        now, StatusBooking.APPROVED);
        assertThat(isExists).isFalse();
    }
}