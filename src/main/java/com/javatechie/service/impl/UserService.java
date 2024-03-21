package com.javatechie.service.impl;

import com.javatechie.dto.UserDto;
import com.javatechie.entity.User;
import com.javatechie.repository.UserInfoRepository;
import com.javatechie.service.IUserService;
import com.javatechie.util.CheckPassWord;
import com.javatechie.util.ConstUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Date;

@Service
public class UserService implements IUserService {
    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public String addUser(UserDto user, Integer role) {
        try {
            if(user.getUsername() == null) {
                return "Username can not null";
            }
            Boolean checkUsername = checkUsernameExist(user.getUsername());
            if(checkUsername) { // email đã tồn tại trong cơ sở dữ liệu
                return "Username already exists ";
            }
            // kiểm tra password có hợp lệ không
            Boolean checkPassword = CheckPassWord.isStrongPassword(user.getPassword());
            if(!checkPassword) { // password không hợp lệ
                return "Password is not valid";
            }

            User userEntity = new User();
            BeanUtils.copyProperties(user, userEntity);
            if(userEntity == null) {
                System.out.print("Add user error!!");
            }
            else {
                userEntity.setRoles(checkRoles(role));
                userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
                userEntity.setCreateDate(new Date(System.currentTimeMillis()));
                userInfoRepository.save(userEntity);
                return "Success";
            }
        }
        catch (Exception e) {
            System.out.println("Đăng ký tài khoản lỗi rồi!!");
            e.printStackTrace();
            return "Fail";
        }
        return null;
    }

//    @Override
//    public List<UserDto> findAllUser(String role) {
//        List<User> listUserEntity = userInfoRepository.findAllByRole(role);
//        List<UserDto> listUserDto = new ArrayList<>();
//        for(User user : listUserEntity) {
//            UserDto userDto = UserConverter.toDto(user);
//            if(userDto != null) {
//                listUserDto.add(userDto);
//            }
//        }
//        return listUserDto;
//    }
//
//    @Override
//    public UserDto findOneUser(Integer id) {
//        User user = userInfoRepository.findById(id).orElse(null);
//        if(user == null) {
//            return null;
//        }
//        UserDto userDto = UserConverter.toDto(user);
//        return userDto;
//    }
//
//    @Override
//    public UserDto updateUser(UserDto userDto, Integer role) {
//        UserDto response = new UserDto();
//        User user;
//        try {
//            if(role.equals(1)) { // TH là admin sửa thông tin của các employee
//                user = userInfoRepository.findById(userDto.getId()).orElse(null);
//            }
//            else { // TH employee chỉnh sửa thông tin của bản thân
//                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//                UserInfoUserDetails userDetails = (UserInfoUserDetails) auth.getPrincipal();
//                user = userInfoRepository.findByUsername(userDetails.getUsername()).orElse(null);
//                if(user == null || user.getId() != userDto.getId()) {
//                    response.setMessage("You cannot change other employees of information!!");
//                    return response;
//                }
//            }
//            if(user == null) {
//                response.setMessage("Can not found user!!");
//                return response;
//            }
//            // thay mật khẩu user
//            if(userDto.getPassword() != null) {
//                String newPassword = userDto.getPassword();
//                Boolean checkPassword = CheckPassWord.isStrongPassword(newPassword);
//                if(!checkPassword) {
//                    response.setMessage("Password is not valid");
//                    return response;
//                }
//                user.setPassword(passwordEncoder.encode(newPassword));
//                userInfoRepository.save(user);
//                response.setMessage("Change password success!!");
//                return response;
//            }
//            user = UserConverter.toEntity(user, userDto);
//            if(user == null) {
//                response.setMessage("Update info user error!!");
//                return response;
//            }
//            user = userInfoRepository.save(user);
//            response = UserConverter.toDto(user);
//            response.setMessage("Update info user success");
//            return response;
//        }
//        catch (Exception e) {
//            response.setMessage("Update info user fail");
//            e.printStackTrace();
//            return response;
//        }
//    }
//
//    @Override
//    public String deleteUser(Integer id) {
//        try {
//            List<Machine> listMachine = machineRepository.findAllByUserId(id);
//            List<Machine> list = new ArrayList<>();
//            for(Machine machine : listMachine) {
//                machine.setUser(null);
//                list.add(machine);
//            }
//            machineRepository.saveAll(list);
//            userInfoRepository.deleteById(id);
//            return "Success";
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//            return "failed";
//        }
//    }
//
//    @Override
//    public UserDto getInfoUser() {
//        try {
//            UserDto responseUser = new UserDto();
//            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//            UserInfoUserDetails userDetails = (UserInfoUserDetails) auth.getPrincipal();
//            User user = userInfoRepository.findByUsername(userDetails.getUsername()).orElse(null);
//            if(user == null) {
//                responseUser.setMessage("Can not found info of you");
//                return responseUser;
//            }
//            responseUser = UserConverter.toDto(user);
//            return responseUser;
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//            System.out.println("Get info user error!!");
//        }
//        return null;
//    }

    // kiểm tra email có tồn tại không
    private Boolean checkUsernameExist(String username) {
        return userInfoRepository.existsByUsername(username);
    }
    // kiểm tra role
    private String checkRoles(Integer role) {
        String roleStr = null;
        if(role == 1) { // role == 1 ==> admin
            roleStr = ConstUtil.ROLE_ADMIN;
        }
        else if (role == 0) { // role == 0 ==> employee
            roleStr = ConstUtil.ROLE_EMPLOYEE;
        }
        else { // role == 2 ==> admin + employee
            roleStr = ConstUtil.ROLE_ADMIN + ", " + ConstUtil.ROLE_EMPLOYEE;
        }
        return roleStr;
    }
}
