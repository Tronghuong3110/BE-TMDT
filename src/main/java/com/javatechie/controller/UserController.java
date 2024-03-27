package com.javatechie.controller;

import com.javatechie.dto.UserDto;
import com.javatechie.service.IUserService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.Array;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api")
public class UserController {

    @Autowired
    private IUserService userService;

    @GetMapping("/admin/users") // ok
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> findAllUser() {
        List<UserDto> listUser = userService.findAllUser();
        if(listUser == null) {
            return ResponseEntity.badRequest().body(new Array[100]);
        }
        return ResponseEntity.ok(listUser);
    }
    @GetMapping("/admin/user/{id}") // ok
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> findOneUserById(@PathVariable("id") Integer id) {
        JSONObject response = userService.findOneUserById(id);
        if(response.get("code").equals(0)) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }
    @GetMapping("/user") // ok
    public ResponseEntity<?> getInfoUserLogin() {
        JSONObject response = userService.getInfoOfUser();
        if(response.get("code").equals(0)) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }
    @PutMapping("/user") // ok
    public ResponseEntity<?> updateInfo(@RequestBody UserDto userDto) {
        JSONObject response = userService.updateUser(userDto);
        if(response.get("code").equals(0)) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/admin/user") // ok
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> deleteUser(@RequestParam("id") Integer id) {
        JSONObject response = userService.deleteUser(id);
        if(response.get("code").equals(0)) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }
}
