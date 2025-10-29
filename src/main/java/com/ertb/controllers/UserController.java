package com.ertb.controllers;

import com.ertb.model.MessageModel;
import com.ertb.model.UserModel;
import com.ertb.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserModel> addUser(@RequestBody UserModel userModel) {
        return ResponseEntity.ok(userService.insertUser(userModel));
    }


    @GetMapping
    public ResponseEntity<List<UserModel>> getAllUser(@RequestParam(required = false) String search) {
        return ResponseEntity.ok(userService.getAllUser(search));
    }

    @PutMapping
    public ResponseEntity<UserModel> editUser(@RequestBody UserModel userModel, @RequestParam String userId) {
        return ResponseEntity.ok(userService.updateUser(userModel, userId));
    }

    @DeleteMapping
    public ResponseEntity<MessageModel> deleteUser(@RequestParam String userId) {
        return ResponseEntity.ok(userService.deleteUser(userId));
    }
}
