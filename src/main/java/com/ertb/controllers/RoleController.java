package com.ertb.controllers;

import com.ertb.model.RoleModel;
import com.ertb.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;


    @GetMapping
    public ResponseEntity<List<RoleModel>> getAllRole() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }
}
