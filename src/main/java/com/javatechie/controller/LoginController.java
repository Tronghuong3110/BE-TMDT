package com.javatechie.controller;

import com.javatechie.config.UserInfoUserDetails;
import com.javatechie.dto.UserDto;
import com.javatechie.service.IUserService;
import com.javatechie.service.impl.JwtService;
import com.javatechie.service.impl.UserService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin("*")
public class LoginController {

    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private IUserService userService;

    // đăng nhập
    @PostMapping("/login")
    public ResponseEntity<?> authenticateAndGetToken(@RequestBody UserDto userDto){
        JSONObject jsonObject = new JSONObject();
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword()));
            if (authentication.isAuthenticated()) {
                UserInfoUserDetails userDetails = (UserInfoUserDetails) authentication.getPrincipal();
                jsonObject.put("token", jwtService.generateToken(userDto.getUsername()));
                jsonObject.put("role", userDetails.getRoles());
                jsonObject.put("name", userDetails.getName());
                jsonObject.put("username", userDetails.getUsername());
                List<Long> products = userService.findAllProductBought(userDetails.getUserId());
                jsonObject.put("listProductBought", products);
                return ResponseEntity.ok(jsonObject);
            }
            jsonObject.put("code", 0);
            jsonObject.put("token", "Invalid user request!");
            return ResponseEntity.badRequest().body(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("code", 0);
            jsonObject.put("token", "Tài khoản hoặc mật khất không chính xác");
            return ResponseEntity.badRequest().body(jsonObject);
        }
    }
    // đăng kí
    @PostMapping("/signup") // role == 0(employee), 1(admin), 3(user)
    public ResponseEntity<?> signIn(@RequestBody UserDto user, @RequestParam("role") Integer role) {
        JSONObject responseSignUp = userService.addUser(user, role);
        if(responseSignUp.get("code").equals(1)) {
            return ResponseEntity.ok(responseSignUp);
        }
        return ResponseEntity.badRequest().body(responseSignUp);
    }
}
