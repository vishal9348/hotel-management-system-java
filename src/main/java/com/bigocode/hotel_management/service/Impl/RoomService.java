package com.bigocode.hotel_management.service.Impl;

import com.bigocode.hotel_management.dto.Response;
import com.bigocode.hotel_management.dto.RoomDTO;
import com.bigocode.hotel_management.entity.Room;
import com.bigocode.hotel_management.exception.NotFoundException;
import com.bigocode.hotel_management.repo.BookingRepo;
import com.bigocode.hotel_management.repo.RoomRepo;
import com.bigocode.hotel_management.service.Interface.IRoomService;
import com.bigocode.hotel_management.service.utils.Utils;
import org.hibernate.engine.spi.Resolution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class RoomService implements IRoomService {

    @Autowired
    private RoomRepo roomRepo;

    @Autowired
    private BookingRepo bookingRepo;

    @Override
    public Response addNewRoom(MultipartFile photo, String roomType, BigDecimal roomPrice, String description) {
        Response response = new Response();
        try{
            Room room = new Room();
            room.setRoomType(roomType);
            room.setRoomPrice(String.valueOf(roomPrice));
            room.setRoomDescription(description);
            Room saveData = roomRepo.save(room);
            RoomDTO roomDTO = Utils.mapRoomEntityToRoomDTO(saveData);
            response.setStatusCode(200);
            response.setMessage("Room Added");
            response.setRoom(roomDTO);
        }
        catch (NotFoundException e){
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        }
        catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error Occurred while Adding room" +e.getMessage());
        }
        return response;
    }

    @Override
    public List<String> getAllRoomTypes() {
        return roomRepo.findDistinctRoomTypes();
    }

    @Override
    public Response getAllRooms() {
        Response response = new Response();
        try{
            List<Room> roomList = roomRepo.findAll(Sort.by(Sort.Direction.DESC, "id"));
            List<RoomDTO> roomDTOList = Utils.mapRoomListEntityToRoomListDTO(roomList);
            response.setStatusCode(200);
            response.setMessage("Room Added");
            response.setRoomList(roomDTOList);
        }
        catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error Occurred while fetching room" +e.getMessage());
        }
        return response;
    }

    @Override
    public Response deleteRoom(Long roomId) {
        Response response = new Response();
        try{
            roomRepo.findById(roomId).orElseThrow(() -> new NotFoundException("User not found with this id"));
            roomRepo.deleteById(roomId);
            response.setStatusCode(200);
            response.setMessage("Room deleted!!!");
        }
        catch (NotFoundException e){
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        }
        catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error Occurred while deleting room" +e.getMessage());
        }
        return response;
    }

    @Override
    public Response updateRoom(Long roomId,String description, String roomType, BigDecimal roomPrice, MultipartFile photo) {
        Response response = new Response();
        try{

            Room room = roomRepo.findById(roomId).orElseThrow(()-> new NotFoundException("Room not found"));

            if(roomType != null) room.setRoomType(roomType);
            if(description != null)room.setRoomDescription(description);
            if(roomPrice != null)room.setRoomPrice(String.valueOf(roomPrice));

            Room updatedRoom = roomRepo.save(room);
            RoomDTO roomDTO = Utils.mapRoomEntityToRoomDTO(updatedRoom);
            response.setStatusCode(200);
            response.setMessage("Room Added");
            response.setRoom(roomDTO);
        }
        catch (NotFoundException e){
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        }
        catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error Occurred while updating room" +e.getMessage());
        }
        return response;
    }

    @Override
    public Response getRoomById(Long roomId) {
        Response response = new Response();
        try{
            Room room = roomRepo.findById(roomId).orElseThrow(() -> new NotFoundException("User not found with this id"));
            RoomDTO roomDTO = Utils.mapRoomEntityToRoomDTOPlusBookings(room);
            response.setStatusCode(200);
            response.setMessage("Room fetched");
            response.setRoom(roomDTO);
        }
        catch (NotFoundException e){
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        }
        catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error Occurred while fetching room" +e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAvailableRoomsByDateAndType(LocalDate checkInDate, LocalDate checkOutDate, String roomType) {
        Response response = new Response();
        try{
            List<Room> availableRooms = roomRepo.findAvailableRoomByDateAndTypes(checkInDate, checkOutDate, roomType);
            List<RoomDTO> roomDTOList = Utils.mapRoomListEntityToRoomListDTO(availableRooms);
            response.setStatusCode(200);
            response.setMessage("Room fetched");
            response.setRoomList(roomDTOList);
        }
        catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error Occurred while fetching room" +e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllAvailableRooms() {
        Response response = new Response();
        try{
            List<Room> roomList = roomRepo.getAllAvailableRoom();
            List<RoomDTO> roomDTOList = Utils.mapRoomListEntityToRoomListDTO(roomList);
            response.setStatusCode(200);
            response.setMessage("Room fetched");
            response.setRoomList(roomDTOList);
        }
        catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error Occurred while fetching room" +e.getMessage());
        }
        return response;
    }
}
