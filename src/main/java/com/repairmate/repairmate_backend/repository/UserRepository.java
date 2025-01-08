package com.repairmate.repairmate_backend.repository;

import com.repairmate.repairmate_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
