package com.javatechie.service;

import com.javatechie.dto.UserDto;

public interface IUserService {
    // thêm mới User(admin hoặc employee)
    String addUser(UserDto user, Integer role);
    // lấy ra toàn bộ user có trong database với role là EMPLOYEE
//    List<UserDto> findAllUser(String role);
//    // lấy ra 1 user trong database(role = EMPLOYEE)
//    UserDto findOneUser(Integer id);
//    UserDto updateUser(UserDto userDto, Integer role);
//    String deleteUser(Integer id);
//    UserDto getInfoUser();
}
