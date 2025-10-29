package com.ertb.repositories;

import com.ertb.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    boolean existsByEmail(String email);

    @Query(value = "SELECT distinct u.* "
            + "FROM users u "
            + "JOIN user_roles ur ON u.user_id = ur.user_id "
            + "JOIN role r ON ur.role_id = r.role_id "
            + "WHERE (:search IS NULL OR LOWER(u.first_name) LIKE LOWER(CONCAT('%', :search, '%'))) "
            + "OR (:search IS NULL OR LOWER(u.last_name) LIKE LOWER(CONCAT('%', :search, '%'))) "
            + "OR (:search IS NULL OR LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%'))) "
            + "OR (:search IS NULL OR LOWER(u.phone_number) LIKE LOWER(CONCAT('%', :search, '%'))) "
            + "OR (:search IS NULL OR LOWER(r.role_name) LIKE LOWER(CONCAT('%', :search, '%'))) ",
        nativeQuery = true)
    List<User> findAllUserBy(@Param("search") String search);

    Optional<User> findByUserId(String userId);

    User findByEmail(String email);
}
