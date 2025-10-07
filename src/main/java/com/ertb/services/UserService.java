package com.ertb.services;

import com.ertb.enumerations.Status;
import com.ertb.exceptions.DataNotFoundException;
import com.ertb.exceptions.DataValidationException;
import com.ertb.mappers.RoleMapper;
import com.ertb.mappers.UserMapper;
import com.ertb.model.MessageModel;
import com.ertb.model.RoleModel;
import com.ertb.model.UserModel;
import com.ertb.model.entities.Role;
import com.ertb.model.entities.User;
import com.ertb.model.entities.UserRole;
import com.ertb.repositories.RoleRepository;
import com.ertb.repositories.UserRepository;
import com.ertb.repositories.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final UserRoleRepository userRoleRepository;

    private final RoleMapper roleMapper;

    private final UserMapper userMapper;



    @Transactional
    public UserModel insertUser(UserModel userModel) {

        if (userRepository.existsByEmail((userModel.getEmail()))) {
            throw new DataValidationException("Email already exists");
        }

        User user = userMapper.userModelToUser(userModel);
//        user.setPassword(encoder.encode(userModel.getPassword()));
        user = userRepository.save(user);

        List<String> roleIdsFromModel = userModel.getRoles().stream().map(r -> r.getRoleId()).toList();
        List<Role> rolesFromEntity = roleRepository.findByRoleIdIn(roleIdsFromModel);
        List<String> roleIdFromEntity = rolesFromEntity.stream().map(r -> r.getRoleId()).toList();
        List<String> invalidRoleId = new ArrayList<>();

        for (String roleIdFromModel : roleIdsFromModel) {
            if (!roleIdFromEntity.contains(roleIdFromModel)) {
                invalidRoleId.add(roleIdFromModel);
            }
        }

        if (!invalidRoleId.isEmpty()) {
            throw new DataValidationException("Invalid role id: " + invalidRoleId);
        }

        List<Role> rolesToSave = rolesFromEntity.stream().filter(r -> roleIdsFromModel.contains(r.getRoleId())).toList();

        for (Role role : rolesToSave) {
            UserRole userRole = new UserRole();
            userRole.setUser(user);
            userRole.setRole(role);
            userRoleRepository.save(userRole);
        }

        UserModel userModelToReturn = userMapper.userToUserModel(user);
        List<UserRole> byUserUserId = userRoleRepository.findByUserUserId(user.getUserId());
        List<RoleModel> roleModels = new ArrayList<>();
        byUserUserId.forEach(ur -> roleModels.add(roleMapper.roleToRoleModel(ur.getRole())));
        userModelToReturn.setRoles(roleModels);

        return userModelToReturn;
    }



    public List<UserModel> getAllUser(String search) {

        List<User> userList = userRepository.findAllUserBy(search);
        List<UserModel> userModelList = userMapper.userListToUserModelList(userList);
        for (UserModel userModel : userModelList) {
            List<UserRole> byUserUserId = userRoleRepository.findByUserUserIdAndStatus(userModel.getUserId(), Status.ACTIVE);
            List<RoleModel> roleModelList = new ArrayList<>();
            byUserUserId.forEach(ur -> roleModelList.add(roleMapper.roleToRoleModel(ur.getRole())));
            userModel.setRoles(roleModelList);
        }

        return userModelList;
    }



    /**
     * Updates a user's information and their associated roles.
     *
     * This method performs the following steps:
     * - Finds the user by userId. Throws DataNotFoundException if not found.
     * - Updates the user entity using data from the provided UserModel.
     * - Saves the updated user to the repository.
     * - Retrieves and validates the list of roles provided in the model.
     * - Compares the current roles of the user with the new list:
     *     - Updates the status of existing roles.
     *     - Adds new roles if they do not already exist.
     *     - Validates that all role IDs exist in the system.
     * - Throws DataValidationException if any role ID is invalid.
     * - Returns the updated UserModel with active roles only.
     *
     * @param userModel the model containing updated user data
     * @param userId the ID of the user to update
     * @return the updated UserModel containing only active roles
     * @throws DataNotFoundException if the user is not found
     * @throws DataValidationException if any of the role IDs in the input are invalid
     */
    @Transactional
    public UserModel updateUser(UserModel userModel, String userId) {

        User user = userRepository.findByUserId(userId).orElseThrow(
                () -> new DataNotFoundException("User not found on :- " + userId));

        userMapper.updateUserFromUserModel(userModel, user);
        user.setUserId(userId);
//        user.setPassword(encoder.encode(userModel.getPassword()));
        User saveUser = userRepository.save(user);

        List<String> roleIdsFromModel = userModel.getRoles().stream().map(r -> r.getRoleId()).toList();
        List<Role> rolesFromEntity = roleRepository.findByRoleIdIn(roleIdsFromModel);
        List<String> roleIdFromEntity = rolesFromEntity.stream().map(r -> r.getRoleId()).toList();

        List<UserRole> existingRolesInUser = userRoleRepository.findByUserUserId(userId);
        List<String> existingRoleIdsInUser = existingRolesInUser.stream().map(ur -> ur.getRole().getRoleId()).toList();

        updateRoleStatus(existingRolesInUser, roleIdsFromModel);

        List<String> notExistingRoleIdsInUser = roleIdsFromModel.stream().filter(
                r -> !existingRoleIdsInUser.contains(r)).toList();

        List<String> invalidRoleId = new ArrayList<>();
        if (!invalidRoleId.isEmpty()) {
            for (String roleId : notExistingRoleIdsInUser) {
                if (!roleIdFromEntity.contains(roleId)) {
                    invalidRoleId.add(roleId);
                }
            }
        }

        if (!invalidRoleId.isEmpty()) {
            throw new DataValidationException("Role is Not Valida :- " + invalidRoleId);
        }

        List<Role> rolesToSave = rolesFromEntity.stream().filter(
                r -> notExistingRoleIdsInUser.contains(r.getRoleId())).toList();

        for (Role role : rolesToSave) {
            UserRole userRole = new UserRole();
            userRole.setUser(saveUser);
            userRole.setRole(role);
            userRoleRepository.save(userRole);
        }

        UserModel userModelToReturn = userMapper.userToUserModel(saveUser);
        List<UserRole> activeUserRole = userRoleRepository.findByUserUserIdAndStatus(saveUser.getUserId(), Status.ACTIVE);
        List<RoleModel> roleModelList = activeUserRole.stream().map(
                ur -> roleMapper.roleToRoleModel(ur.getRole())).toList();
        userModelToReturn.setRoles(roleModelList);

        return userModelToReturn;
    }


    public MessageModel deleteUser(String userId) {
        User user = userRepository.findByUserId(userId).orElseThrow(
                () -> new DataNotFoundException("User not found on :- " + userId));

        List<UserRole> userRoleList = userRoleRepository.findByUserUserId(userId);

        user.setStatus(Status.INACTIVE);
        userRoleList.forEach(ur -> ur.setStatus(Status.INACTIVE));
        userRepository.save(user);
        userRoleRepository.saveAll(userRoleList);
        MessageModel messageModel = new MessageModel();
        messageModel.setMessage("User deleted successfully");

        return messageModel;
    }


    /**
     * Updates the status of user roles based on the provided list of role IDs.
     *
     * This method compares the existing roles assigned to a user with a new list of role IDs:
     * - If a role is present in the new list and currently marked as INACTIVE, it is activated.
     * - If a role is not present in the new list and currently marked as ACTIVE, it is deactivated.
     *
     * All changes are saved to the repository.
     *
     * @param existingRoleInUser the list of UserRole entities currently assigned to the user
     * @param roleIdsFromModel the list of role IDs that should be active for the user
     */
    private void updateRoleStatus(List<UserRole> existingRoleInUser, List<String> roleIdsFromModel) {
        for (UserRole userRole : existingRoleInUser) {
            String roleId = userRole.getRole().getRoleId();

            if (roleIdsFromModel.contains(roleId)) {
                if (userRole.getStatus().equals(Status.INACTIVE)) {
                    userRole.setStatus(Status.ACTIVE);
                    userRoleRepository.save(userRole);
                }
            }else {
                if (userRole.getStatus().equals(Status.ACTIVE)) {
                    userRole.setStatus(Status.INACTIVE);
                    userRoleRepository.save(userRole);
                }
            }
        }
    }
}
