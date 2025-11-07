package com.ertb.services;

import com.ertb.enumerations.PermissionEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;


    /**
     * Checks if the currently logged-in personnel has the specified permission.
     *
     * @param validatePermissionEnum The permission to validate.
     * @return True if the personnel has the permission, false otherwise.
     */
    @Transactional(readOnly = true)
    public boolean hassPermission(PermissionEnum validatePermissionEnum) {

        return userService.getLoginUser().getUserRoles().stream()
                .flatMap(userRole -> userRole.getRole().getRolePermission().stream())
                .map(rolePermission -> rolePermission.getPermission().getPermissionName())
                .collect(Collectors.toSet()).contains(validatePermissionEnum);
    }
}
