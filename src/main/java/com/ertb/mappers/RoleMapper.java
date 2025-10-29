package com.ertb.mappers;

import com.ertb.model.RoleModel;
import com.ertb.model.entities.Role;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
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
public interface RoleMapper {


    List<RoleModel> roleListToRoleModelList(List<Role> roles);

    RoleModel roleToRoleModel(Role role);
}
