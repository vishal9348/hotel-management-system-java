package com.bigocode.hotel_management.service.Impl;

import com.bigocode.hotel_management.dto.BookingsDTO;
import com.bigocode.hotel_management.dto.Response;
import com.bigocode.hotel_management.entity.Booking;
import com.bigocode.hotel_management.entity.Room;
import com.bigocode.hotel_management.entity.User;
import com.bigocode.hotel_management.exception.NotFoundException;
import com.bigocode.hotel_management.repo.BookingRepo;
import com.bigocode.hotel_management.repo.RoomRepo;
import com.bigocode.hotel_management.repo.UserRepo;
import com.bigocode.hotel_management.service.Interface.IBookingService;
import com.bigocode.hotel_management.service.Interface.IRoomService;
import com.bigocode.hotel_management.service.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.util.List;

@Service
public class BookingServiceImpl implements IBookingService {

    @Autowired
    private BookingRepo bookingRepo;

    @Autowired
    private RoomRepo roomRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private IRoomService iRoomService;

    @Override
    public Response saveBooking(Long roomId, Long userId, Booking bookingRequest) {
        Response response = new Response();

        try{
            if(bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())){
                throw new IllegalArgumentException("Checking date must come after check out date");
            }

            Room room = roomRepo.findById(roomId).orElseThrow(()-> new NotFoundException("Room not found"));
            User user = userRepo.findById(userId).orElseThrow(()-> new NotFoundException("User not found"));

            List<Booking> existingBooking = room.getBookings();
            
            if(!roomAvailable(bookingRequest, existingBooking)){
                throw new NotFoundException("Room not Available for selected filter");
            }

            bookingRequest.setRoom(room);
            bookingRequest.setUser(user);
            String bookingConfirmationCode = Utils.generateConfirmationCodeForBooking(10);
            bookingRequest.setBookingConfirmationCode(bookingConfirmationCode);
            bookingRepo.save(bookingRequest);
            response.setStatusCode(200);
            response.setMessage("Your booking is confirmed");
            response.setBookingConfirmationCode(bookingConfirmationCode);

        }
        catch (NotFoundException e){
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        }
        catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error Occurred while saving booking details"+ e.getMessage());
        }

        return response;
    }

    @Override
    public Response findBookingByConfirmationCode(String confirmationCode) {
        Response response = new Response();

        try{
            Booking booking = bookingRepo.findByBookingConfirmationCode(confirmationCode).orElseThrow(() -> new NotFoundException("No booking found with this Confirmation code"));
            BookingsDTO bookingsDTO = Utils.mapBookingEntityToBookingDTO(booking);
            response.setStatusCode(200);
            response.setMessage("Record fetched");
            response.setBooking(bookingsDTO);
        }
        catch (NotFoundException e){
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        }
        catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error Occurred while fetching booking details"+ e.getMessage());
        }

        return response;
    }

    @Override
    public Response getAllBookings() {
        Response response = new Response();
        try{
            List<Booking> booking = bookingRepo.findAll(Sort.by(Sort.Direction.DESC, "id"));
            List<BookingsDTO> bookingsDTO = Utils.mapBookingListEntityToBookingsListDTO(booking);
            response.setStatusCode(200);
            response.setMessage("Record fetched");
            response.setBookingList(bookingsDTO);
        }
        catch (NotFoundException e){
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        }
        catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error Occurred while fetching booking details"+ e.getMessage());
        }

        return response;
    }

    @Override
    public Response cancelBooking(Long bookingId) {
        Response response = new Response();
        try{
            bookingRepo.findById(bookingId).orElseThrow(()-> new NotFoundException("Booing not found"));
            bookingRepo.deleteById(bookingId);
            response.setStatusCode(200);
            response.setMessage("Booking Canceled");
        }
        catch (NotFoundException e){
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        }
        catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error Occurred while canceling booking details"+ e.getMessage());
        }
        return response;
    }

    private boolean roomAvailable(Booking bookingRequest, List<Booking> existingBookings) {

      return existingBookings.stream()
        .noneMatch(existingBooking ->
                bookingRequest.getCheckInDate().equals(existingBooking.getCheckInDate())
                        || bookingRequest.getCheckOutDate().isBefore(existingBooking.getCheckOutDate())

                        || (bookingRequest.getCheckInDate().isAfter(existingBooking.getCheckInDate())
                        &&  bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate()))

                        || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())
                        && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckOutDate()))

                        || (bookingRequest.getCheckOutDate().isBefore(existingBooking.getCheckInDate())
                        && bookingRequest.getCheckOutDate().isAfter(existingBooking.getCheckOutDate()))

                        || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                        && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckOutDate()))

                        || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                        &&  bookingRequest.getCheckOutDate().equals(bookingRequest.getCheckInDate()))
        );
    }
}
