package com.example.petparadisebe.Repositories;

import com.example.petparadisebe.Entities.Product;
import com.example.petparadisebe.Entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    Optional<User> findByEmail(String email);
    @Query(value = "SELECT users.*, JSON_ARRAYAGG(roles.name) as roles FROM users \n" +
            "JOIN user_role ON users.id = user_role.user_id\n" +
            "JOIN roles ON user_role.role_id = roles.id GROUP BY users.id, users.username", nativeQuery = true)
    List<User> findUsernamesAndRoleNames();

    @Query("select j from User j where j.username like %:key%")
    Page<User> searchUser(@Param("key") String textSearch, Pageable pageable);

}
