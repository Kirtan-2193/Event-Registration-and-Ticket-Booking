package com.ertb.mappers;

import com.ertb.model.UserModel;
import com.ertb.model.UserTicket;
import com.ertb.model.entities.User;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(
        componentModel = SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        builder = @Builder(disableBuilder = true),
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL
)
public interface UserMapper {

    User userModelToUser(UserModel userModel);

    UserModel userToUserModel(User user);

    List<UserModel> userListToUserModelList(List<User> users);

    void updateUserFromUserModel(UserModel userModel, @MappingTarget User user);

    UserTicket userToUserTicket(User user);
}
