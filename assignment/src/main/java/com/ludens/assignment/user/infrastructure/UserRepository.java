package com.ludens.assignment.user.infrastructure;

import com.ludens.assignment.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByUsernameAndDeletedAtIsNull(String username);

    Optional<User> findByIdAndDeletedAtIsNull(String id);

    boolean existsByUsernameAndDeletedAtIsNull(String username);
}
