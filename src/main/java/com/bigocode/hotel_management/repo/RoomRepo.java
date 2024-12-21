package com.bigocode.hotel_management.repo;

import com.bigocode.hotel_management.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.time.LocalDate;
import java.util.List;

@EnableJpaRepositories
public interface RoomRepo extends JpaRepository<Room, Long> {

    @Query("SELECT DISTINCT r.roomType FROM Room r")
    List<String> findDistinctRoomTypes();

    @Query("SELECT r FROM Room r WHERE r.id NOT IN(SELECT b.room.id FROM Booking b)")
    List<Room> getAllAvailableRoom();

    @Query("SELECT r FROM Room r WHERE r.roomType LIKE %:roomType% AND r.id NOT IN (SELECT b.room.id FROM Booking b WHERE (b.checkInDate <= :checkInDate) AND (b.checkOutDate >= :checkOutDate))")
    List<Room> findAvailableRoomByDateAndTypes(LocalDate checkInDate, LocalDate checkOutDate, String roomType);

}
