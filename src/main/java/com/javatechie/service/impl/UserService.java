package com.javatechie.service.impl;

import com.javatechie.api.ApiAddInfoToBroker;
import com.javatechie.config.UserInfoUserDetails;
import com.javatechie.dto.UserDto;
import com.javatechie.entity.User;
import com.javatechie.repository.OrderRepository;
import com.javatechie.repository.UserInfoRepository;
import com.javatechie.service.IUserService;
import com.javatechie.util.CheckPassWord;
import com.javatechie.util.ConstUtil;
import com.javatechie.util.MapperUtil;
import org.json.simple.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements IUserService {
    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private OrderRepository orderRepository;
    @Override
    @Transactional
    public JSONObject addUser(UserDto user, Integer role) {
        JSONObject response = new JSONObject();
        try {
            response.put("code", 0);
            response.put("user", null);
            if(user.getUsername() == null) {
                response.put("message", "Username can not null");
                return response;
            }
            Boolean checkUsername = checkUsernameExist(user.getUsername());
            if(checkUsername) { // email đã tồn tại trong cơ sở dữ liệu
                response.put("message", "Username already exists ");
                return response;
            }
            // kiểm tra password có hợp lệ không
            Boolean checkPassword = CheckPassWord.isStrongPassword(user.getPassword());
            if(!checkPassword) { // password không hợp lệ
                response.put("message", "Password is not valid");
                return response;
            }
            User userEntity = new User();
            BeanUtils.copyProperties(user, userEntity);
            userEntity.setRoles(checkRoles(role));
            userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
            userEntity.setCreateDate(new Date(System.currentTimeMillis()));
            userEntity.setDeleted(0);
            userEntity = userInfoRepository.save(userEntity);
            if(userEntity.getRoles().contains("ADMIN") || userEntity.getRoles().contains("EMPLOYEE")) {
                // thêm mới topic vào db
                String topic = "buy";
                // thêm mới user vào broker
                boolean addUserToBroker = ApiAddInfoToBroker.addUserToBroker(user.getUsername(), user.getPassword());
                if(!addUserToBroker) {
                    response.put("code", 0);
                    response.put("message", "Đăng ký tài khoản lỗi !!");
                    userInfoRepository.deleteById(userEntity.getId());
                    return response;
                }
                // thêm mới role cho user để đăng ký subcribe và publish với chính topic của user
                String responseAddRole = ApiAddInfoToBroker.addRuleToBroker(user.getUsername(), topic);
            }
            BeanUtils.copyProperties(userEntity, user);
            response.put("code", 1);
            response.put("message", "Đăng ký tài khoản thành công !!");
            response.put("user", user);
            return response;
        }
        catch (Exception e) {
            e.printStackTrace();
            response.put("code", 0);
            response.put("message", "Đăng ký tài khoản lỗi !!");
        }
        return response;
    }

    @Override
    public List<UserDto> findAllUser() {
        try {
            List<User> listUserEntity = userInfoRepository.findAllByDeleted(0);
            List<UserDto> listUserDto = new ArrayList<>();
            for(User user : listUserEntity) {
                UserDto userDto = new UserDto();
                BeanUtils.copyProperties(user, userDto);
                userDto.setPassword(null);
                listUserDto.add(userDto);
            }
            return listUserDto;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public JSONObject findOneUserById(Integer id) {
        JSONObject response = new JSONObject();
        try {
            UserDto userDto = new UserDto();
            User user = userInfoRepository.findByIdAndDeleted(id, 0).orElse(null);
            if(user == null) {
                response.put("code", 0);
                response.put("message", "Can not found user");
                return response;
            }
            BeanUtils.copyProperties(user, userDto);
            response.put("code", 1);
            response.put("message", userDto);
        }
        catch (Exception e) {
            e.printStackTrace();
            response.put("code", 1);
            response.put("message", "Get info user error");
        }
        return response;
    }

    @Override
    public JSONObject getInfoOfUser() {
        JSONObject response = new JSONObject();
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            UserInfoUserDetails userDetails = (UserInfoUserDetails) auth.getPrincipal();
            User user = userInfoRepository.findByUsernameAndDeleted(userDetails.getUsername(), 0).orElse(null);
            UserDto userDto = new UserDto();
            if(user == null) {
                response.put("code", 0);
                response.put("message", "Can not found user");
                return response;
            }
            BeanUtils.copyProperties(user, userDto);
            response.put("code", 1);
            response.put("message", userDto);
        }
        catch (Exception e) {
            e.printStackTrace();
            response.put("code", 1);
            response.put("message", "Get info user error");
        }
        return response;
    }

    @Override
    public JSONObject updateUser(UserDto userDto) {
        JSONObject response = new JSONObject();
        User user = new User();
        try {
            UserInfoUserDetails userInfo = new UserInfoUserDetails();
            // Th chỉnh sửa thông tin user đang đăng nhập
            if(userDto.getId() == null) {
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                userInfo = (UserInfoUserDetails) auth.getPrincipal();
                user = userInfoRepository.findByUsernameAndDeleted(userInfo.getUsername(), 0).orElse(null);
            }
            // TH chỉnh sửa thông tin user do admin chính sửa
            else {
                user = userInfoRepository.findById(userDto.getId()).orElse(null);
            }
            if(user == null) {
                response.put("code", 0);
                response.put("message", "Không thể thay đổi thông tin của user !!");
                return response;
            }
            // TH thay đổi password ==> check lại điều kiện password
            if (userDto.getPassword() != null) {
                boolean checkPassword = CheckPassWord.isStrongPassword(userDto.getPassword());
                if(!checkPassword) {
                    response.put("code", 0);
                    response.put("message", "Mật khẩu không hợp lệ !!");
                    return response;
                }
                // kiểm tra mật khẩu cũ nhập vào có giống với mật khẩu hiện tại không
                if(!passwordEncoder.matches(userDto.getPasswordOld(), user.getPassword())) {
                    response.put("code", 0);
                    response.put("message", "Mật khẩu không đúng !!");
                    return response;
                }
                user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            }
            else {
                user = convertFromDtoToEntity(userDto, user);
            }
            user.setModifiedDate(new Date(System.currentTimeMillis()));
            userInfoRepository.save(user);
            response.put("code", 1);
            response.put("message", "Thay đổi thông tin thành công !!");
        }
        catch (Exception e) {
            e.printStackTrace();
            response.put("code", 0);
            response.put("message", e.getMessage());
        }
        return response;
    }

    @Override
    public JSONObject deleteUser(Integer id) {
        JSONObject response = new JSONObject();
        try {
            User user = userInfoRepository.findByIdAndDeleted(id, 0).orElse(null);
            if(user == null) {
                response.put("code", 0);
                response.put("message", "Không tìm thấy người dùng có id = " + id);
                return response;
            }
            user.setDeleted(1);
            userInfoRepository.save(user);
            response.put("code", 1);
            response.put("message", "Xóa tài khoản thành công !!");
        }
        catch (Exception e) {
            e.printStackTrace();
            response.put("code", 0);
            response.put("message", "Xóa tài khoản thất bại !!");
        }
        return response;
    }

    @Override
    public List<Long> findAllProductBought(Integer userId) {
        try {
            return orderRepository.findALlProductBoughtByUser(userId);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    // kiểm tra username có tồn tại không
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
        else { // role == 2 ==> user
            roleStr = ConstUtil.ROLE_USER;
        }
        return roleStr;
    }

    private User convertFromDtoToEntity(UserDto userDto, User user) {
        try {
            ModelMapper mapper = MapperUtil.configModelMapper();
            mapper.map(userDto, user);
            return user;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
