package com.bigocode.hotel_management.service.Interface;

import com.bigocode.hotel_management.dto.Response;
import com.bigocode.hotel_management.entity.Booking;

public interface IBookingService {

    Response saveBooking(Long roomId, Long userId, Booking bookingRequest);

    Response findBookingByConfirmationCode(String confirmationCode);

    Response getAllBookings();

    Response cancelBooking(Long bookingId);
}
