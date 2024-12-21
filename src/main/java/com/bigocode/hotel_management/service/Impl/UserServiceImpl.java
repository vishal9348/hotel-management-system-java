package com.bigocode.hotel_management.service.Impl;

import com.bigocode.hotel_management.dto.LoginRequest;
import com.bigocode.hotel_management.dto.Response;
import com.bigocode.hotel_management.dto.UserDTO;
import com.bigocode.hotel_management.entity.User;
import com.bigocode.hotel_management.exception.NotFoundException;
import com.bigocode.hotel_management.repo.UserRepo;
import com.bigocode.hotel_management.service.Interface.IUserService;
import com.bigocode.hotel_management.service.utils.JWTUtils;
import com.bigocode.hotel_management.service.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;


    @Override
    public Response register(User user) {
        Response response = new Response();

        try{
            if(user.getRole() == null || user.getRole().isBlank()){
                user.setRole("USER");
            }
            if(userRepo.existsByEmail(user.getEmail())){
                throw new NotFoundException("User already registered");
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User saveUser = userRepo.save(user);
            UserDTO userDTO = Utils.mapUserEntityToUserDTO(saveUser);
            response.setStatusCode(200);
            response.setUser(userDTO);
        }
        catch (NotFoundException e){
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occured while saving the user" +e.getMessage());
        }
        return response;
    }

    @Override
    public Response login(LoginRequest loginRequest) {

        Response response = new Response();
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            var user = userRepo.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new NotFoundException("User not registered!!!"));

            var token = jwtUtils.generateToken(user);
            response.setStatusCode(200);
            response.setToken(token);
            response.setRole(user.getRole());
            response.setExpirationTime("7 Days");
            response.setMessage("Login successfull!!!");
        }
        catch (NotFoundException e){
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        }
        catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error occured while login the user" +e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllUsers() {
        Response response = new Response();

        List<User> users = userRepo.findAll();
        List<UserDTO> userDTOList = Utils.mapUserListEntityToUserListDTO(users);
        response.setStatusCode(200);
        response.setMessage("Records fetched");
        response.setUserList(userDTOList);
        return response;
    }

    @Override
    public Response getUserBookingHistory(String userId) {

        Response response = new Response();

        try{
            User user = userRepo.findById(Long.valueOf(userId)).orElseThrow(() -> new NotFoundException("User not found with this Id"));
            UserDTO userDTO = Utils.mapUserEntityToUserDTOPlusUserBookingsAndRoom(user);
            response.setStatusCode(200);
            response.setMessage("Fetched user history");
            response.setUser(userDTO);
        }
        catch (NotFoundException e){
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        }
        catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error occured while fetching booking history" +e.getMessage());
        }
        return response;
    }

    @Override
    public Response deleteUser(String userId) {
        Response response = new Response();

        try{
            User user = userRepo.findById(Long.valueOf(userId)).orElseThrow(() -> new NotFoundException("User not found with this Id"));
            userRepo.deleteById(Long.valueOf(userId));
            response.setStatusCode(200);
            response.setMessage("User deleted");
        }
        catch (NotFoundException e){
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        }
        catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error occured while deleting user" +e.getMessage());
        }
        return response;
    }

    @Override
    public Response getUserById(String userId) {
        Response response = new Response();

        try{
            User user = userRepo.findById(Long.valueOf(userId)).orElseThrow(() -> new NotFoundException("User not found with this Id"));
            UserDTO userDTO = Utils.mapUserEntityToUserDTO(user);
            response.setStatusCode(200);
            response.setMessage("User fetched");
            response.setUser(userDTO);
        }
        catch (NotFoundException e){
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        }
        catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error occured while fetching User" +e.getMessage());
        }
        return response;
    }

    @Override
    public Response getMyInfo(String email) {
        Response response = new Response();

        try{
            User user = userRepo.findByEmail(email).orElseThrow(() -> new NotFoundException("No user associated with this email"));
            UserDTO userDTO = Utils.mapUserEntityToUserDTOPlusUserBookingsAndRoom(user);
            response.setStatusCode(200);
            response.setMessage("Fetched user details");
            response.setUser(userDTO);
        }
        catch (NotFoundException e){
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        }
        catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error occured while fetching booking history" +e.getMessage());
        }
        return response;
    }
}
