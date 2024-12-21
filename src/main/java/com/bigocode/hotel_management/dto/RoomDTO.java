package com.bigocode.hotel_management.dto;

import com.bigocode.hotel_management.entity.Booking;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoomDTO {

    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public void setRoomPrice(String roomPrice) {
        this.roomPrice = roomPrice;
    }

    public void setRoomPhotoUrl(String roomPhotoUrl) {
        this.roomPhotoUrl = roomPhotoUrl;
    }

    public void setRoomDescription(String roomDescription) {
        this.roomDescription = roomDescription;
    }

    public void setBookings(List<BookingsDTO> bookings) {
        this.bookings = bookings;
    }

    private String roomType;

    public Long getId() {
        return id;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getRoomPrice() {
        return roomPrice;
    }

    public String getRoomPhotoUrl() {
        return roomPhotoUrl;
    }

    public String getRoomDescription() {
        return roomDescription;
    }

    public List<BookingsDTO> getBookings() {
        return bookings;
    }

    private String roomPrice;
    private String roomPhotoUrl;
    private String roomDescription;
    private List<BookingsDTO> bookings;
}
