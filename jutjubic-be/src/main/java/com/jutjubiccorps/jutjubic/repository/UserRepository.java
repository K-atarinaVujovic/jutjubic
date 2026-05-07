package com.jutjubiccorps.jutjubic.repository;

import com.jutjubiccorps.jutjubic.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findOneByUsername(String username);
    User findOneById(Long id);
    User findOneByEmail(String email);
    User findOneByValidationToken(String token);
    boolean existsByUsername(String username);
    boolean existsById(Long id);
    boolean existsByValidationToken(String token);
}
