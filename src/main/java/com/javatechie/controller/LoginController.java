package com.javatechie.controller;

import com.javatechie.config.UserInfoUserDetails;
import com.javatechie.dto.UserDto;
import com.javatechie.service.impl.JwtService;
import com.javatechie.service.impl.UserService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin("*")
public class LoginController {

    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;

    // đăng nhập
    @PostMapping("/login")
    public ResponseEntity<?> authenticateAndGetToken(@RequestBody UserDto userDto){
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword()));
            if (authentication.isAuthenticated()) {
                UserInfoUserDetails userDetails = (UserInfoUserDetails) authentication.getPrincipal();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("token", jwtService.generateToken(userDto.getUsername()));
                jsonObject.put("role", userDetails.getRoles());
                return ResponseEntity.ok(jsonObject);
            }
            return ResponseEntity.badRequest().body("Invalid user request !");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Tài khoản hoặc mật khất không chính xác");
        }
    }

    @PostMapping("/signup") // role == 0(employee), 1(admin), 2(admin + employee)
    public ResponseEntity<?> signIn(@RequestBody UserDto user, @RequestParam("role") Integer role) {
        String responseSignUp = userService.addUser(user, role);
        if(responseSignUp.equals("Success")) {
            return ResponseEntity.ok("Sign up account success");
        }
        return ResponseEntity.badRequest().body(responseSignUp);
    }


}
