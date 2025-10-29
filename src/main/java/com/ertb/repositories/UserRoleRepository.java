package com.ertb.repositories;

import com.ertb.enumerations.Status;
import com.ertb.model.entities.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRole, String> {

    List<UserRole> findByUserUserId(String userId);

    List<UserRole> findByUserUserIdAndStatus(String userId, Status status);
}
