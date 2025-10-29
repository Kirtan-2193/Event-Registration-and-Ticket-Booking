package com.ertb.services;

import com.ertb.mappers.RoleMapper;
import com.ertb.model.RoleModel;
import com.ertb.model.entities.Role;
import com.ertb.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    private final RoleMapper roleMapper;


    public List<RoleModel> getAllRoles() {
        List<Role> roles = roleRepository.findAll();

        return roleMapper.roleListToRoleModelList(roles);
    }
}
