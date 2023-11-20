package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.data.StatusBooking;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.user.dto.UserResponseDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.intThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class BookingControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final BookingMapper bookingMapper;
    @MockBean
    private final BookingService mockBookingService;

    private final LocalDateTime now = LocalDateTime.now();
    private final BookingDto booking = BookingDto.builder()
            .id(1L)
            .start(now.plusHours(1))
            .end(now.plusDays(10))
            .item(ItemResponseDto.builder()
                    .id(1L)
                    .name("item")
                    .description("item")
                    .available(true)
                    .owner(new UserResponseDto(1L, "owner", "owner@email.ru"))
                    .build()
            )
            .booker(new UserResponseDto(2L, "name2", "name2@email.ru"))
            .status(StatusBooking.WAITING)
            .build();


    @Test
    void createBooking() throws Exception {
        BookingRequestDto bookingRequest = BookingRequestDto.builder()
                .itemId(1L).start(now.plusHours(1)).end(now.plusDays(10)).build();
        when(mockBookingService.createBooking(eq(2L), eq(bookingRequest)))
                .thenReturn(booking);

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 2)
                        .content(objectMapper.writeValueAsString(bookingRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(booking.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(booking.getStatus().toString())));
    }

    @Test
    void approvalBooking() throws Exception {

        BookingDto approvalBooking = BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(booking.getItem())
                .booker(booking.getBooker())
                .status(StatusBooking.APPROVED)
                .build();

        when(mockBookingService.approvalBooking(eq(1L), eq(1L), eq(true)))
                .thenReturn(approvalBooking);

        mockMvc.perform(patch("/bookings/{bookingId}", booking.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .queryParam("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(booking.getId()), Long.class))
                .andExpect(jsonPath("$.start",
                        is(booking.getStart().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.end",
                        is(booking.getEnd().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.status", is(StatusBooking.APPROVED.toString())));
    }

    @Test
    void getBookingById() throws Exception {
        when(mockBookingService.getBookingById(eq(2L), eq(1L)))
                .thenReturn(booking);
        mockMvc.perform(get("/bookings/{bookingId}", booking.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(booking.getId()), Long.class))
                .andExpect(jsonPath("$.start",
                        is(booking.getStart().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.end",
                        is(booking.getEnd().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.status", is(booking.getStatus().toString())));
    }

    @Test
    void getBookings() throws Exception {
        when(mockBookingService.getBookings(eq(2L), eq("ALL"),
                intThat(from -> from > -1), intThat(size -> size > 0)))
                .thenReturn(List.of(booking));
        mockMvc.perform(get("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam("from", "0")
                        .queryParam("size", "10")
                        .queryParam("state", "ALL")
                        .header("X-Sharer-User-Id", 2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(booking.getId()), Long.class));
    }

    @Test
    void getBookingsByOwnerItemId() throws Exception {
        when(mockBookingService.getBookingsByOwnerItemId(eq(1L), eq("ALL"),
                intThat(from -> from > -1), intThat(size -> size > 0)))
                .thenReturn(List.of(booking));
        mockMvc.perform(get("/bookings/owner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam("from", "0")
                        .queryParam("size", "10")
                        .queryParam("state", "ALL")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(booking.getId()), Long.class));
    }
}