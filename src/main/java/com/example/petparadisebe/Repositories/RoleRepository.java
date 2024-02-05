package com.example.petparadisebe.Repositories;

import com.example.petparadisebe.Entities.Role;
import com.example.petparadisebe.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
Role findByName(String role);
}
