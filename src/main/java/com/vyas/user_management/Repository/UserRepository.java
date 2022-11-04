package com.vyas.user_management.Repository;

import com.vyas.user_management.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsByEmail(String email);

    User findByEmail(String email);

    User findByEmailAndNumber(String email, String number);
}
