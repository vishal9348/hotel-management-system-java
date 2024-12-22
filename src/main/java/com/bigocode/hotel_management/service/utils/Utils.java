package com.bigocode.hotel_management.service.utils;

import com.bigocode.hotel_management.dto.BookingsDTO;
import com.bigocode.hotel_management.dto.RoomDTO;
import com.bigocode.hotel_management.dto.UserDTO;
import com.bigocode.hotel_management.entity.Booking;
import com.bigocode.hotel_management.entity.Room;
import com.bigocode.hotel_management.entity.User;

import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;

public class Utils {

    private static final String ALPHANUMERIC_STRING = "AJNUBWINFIWJKJNIUNEIUN65959894898";

    private static final SecureRandom secureRandom = new SecureRandom();

    public static String generateConfirmationCodeForBooking(int length){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i=0; i<length; i++){
            int randomIndex = secureRandom.nextInt(ALPHANUMERIC_STRING.length());
            char randomChar = ALPHANUMERIC_STRING.charAt(randomIndex);
            stringBuilder.append(randomChar);
        }
        return stringBuilder.toString();
    }

    public static UserDTO mapUserEntityToUserDTO(User user){
        UserDTO userDTO = new UserDTO();

        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setRole(user.getRole());

        return userDTO;
    }

    public static RoomDTO mapRoomEntityToRoomDTO(Room room){
        RoomDTO roomDTO = new RoomDTO();

        roomDTO.setId(room.getId());
        roomDTO.setRoomType(room.getRoomType());
        roomDTO.setRoomPrice(room.getRoomPrice());
        roomDTO.setRoomPhotoUrl(room.getRoomPhotoUrl());
        roomDTO.setRoomDescription(room.getRoomDescription());

        return roomDTO;
    }

    public static RoomDTO mapRoomEntityToRoomDTOPlusBookings(Room room){
        RoomDTO roomDTO = new RoomDTO();

        roomDTO.setId(room.getId());
        roomDTO.setRoomType(room.getRoomType());
        roomDTO.setRoomPrice(room.getRoomPrice());
        roomDTO.setRoomPhotoUrl(room.getRoomPhotoUrl());
        roomDTO.setRoomDescription(room.getRoomDescription());

        if(room.getBookings() != null){
            roomDTO.setBookings(room.getBookings().stream().map(Utils::mapBookingEntityToBookingDTO).collect(Collectors.toList()));
        }

        return roomDTO;
    }

    public static BookingsDTO mapBookingEntityToBookingDTO(Booking booking) {

        BookingsDTO bookingsDTO=new BookingsDTO();

        bookingsDTO.setId(booking.getId());
        bookingsDTO.setCheckInDate(booking.getCheckInDate());
        bookingsDTO.setCheckOutDate(booking.getCheckOutDate());
        bookingsDTO.setNumOfAdults(booking.getNumOfAdults());
        bookingsDTO.setNumOfChildren(booking.getNumOfChildren());
        bookingsDTO.setTotalNumOfGuest(booking.getTotalNumOfGuest());
        bookingsDTO.setBookingConfirmationCode(booking.getBookingConfirmationCode());

        return bookingsDTO;
    }

    public static UserDTO mapUserEntityToUserDTOPlusUserBookingsAndRoom(User user){
        UserDTO userDTO = new UserDTO();

        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setRole(user.getRole());

        if(!user.getBookings().isEmpty()){
            userDTO.setBookings(user.getBookings().stream().map(booking -> mapBookingEntityToBookingDTOPlusBookedRoom(booking, false)).collect(Collectors.toList()));
        }

        return userDTO;
    }

    public static BookingsDTO mapBookingEntityToBookingDTOPlusBookedRoom(Booking booking, boolean mapUser){
        BookingsDTO bookingsDTO=new BookingsDTO();

        bookingsDTO.setId(booking.getId());
        bookingsDTO.setCheckInDate(booking.getCheckInDate());
        bookingsDTO.setCheckOutDate(booking.getCheckOutDate());
        bookingsDTO.setNumOfAdults(booking.getNumOfAdults());
        bookingsDTO.setNumOfChildren(booking.getNumOfChildren());
        bookingsDTO.setTotalNumOfGuest(booking.getTotalNumOfGuest());
        bookingsDTO.setBookingConfirmationCode(booking.getBookingConfirmationCode());

        if(mapUser){
            bookingsDTO.setUser(Utils.mapUserEntityToUserDTO(booking.getUser()));
        }

        if(booking.getRoom() != null){
            RoomDTO roomDTO = new RoomDTO();

            roomDTO.setId(booking.getRoom().getId());
            roomDTO.setRoomType(booking.getRoom().getRoomType());
            roomDTO.setRoomPrice(booking.getRoom().getRoomPrice());
            roomDTO.setRoomPhotoUrl(booking.getRoom().getRoomPhotoUrl());
            roomDTO.setRoomDescription(booking.getRoom().getRoomDescription());
            bookingsDTO.setRoom(roomDTO);
        }
        return  bookingsDTO;
    }

    public static List<UserDTO> mapUserListEntityToUserListDTO(List<User> userList){
        return userList.stream().map(Utils::mapUserEntityToUserDTO).collect(Collectors.toList());
    }

    public static List<RoomDTO> mapRoomListEntityToRoomListDTO(List<Room> roomList){
        return roomList.stream().map(Utils::mapRoomEntityToRoomDTO).collect(Collectors.toList());
    }

    public static List<BookingsDTO> mapBookingListEntityToBookingsListDTO(List<Booking> bookingsList){
        return bookingsList.stream().map(Utils::mapBookingEntityToBookingDTO).collect(Collectors.toList());
    }
}
