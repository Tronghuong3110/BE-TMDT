package com.javatechie.service;

import com.javatechie.dto.UserDto;
import org.json.simple.JSONObject;

import java.util.List;

public interface IUserService {
    String addUser(UserDto user, Integer role);
    List<UserDto> findAllUser();
    JSONObject findOneUserById(Integer id);
    JSONObject getInfoOfUser();
    JSONObject updateUser(UserDto userDto);
    JSONObject deleteUser(Integer id);
}
