package com.bigocode.hotel_management.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {

    private int statusCode;
    private String message;
    private String token;
    private String role;
    private String expirationTime;
    private String bookingConfirmationCode;
    private UserDTO user;
    private RoomDTO room;
    private BookingsDTO booking;
    private List<UserDTO> userList;
    private List<RoomDTO> roomList;
    private List<BookingsDTO> bookingList;
}
