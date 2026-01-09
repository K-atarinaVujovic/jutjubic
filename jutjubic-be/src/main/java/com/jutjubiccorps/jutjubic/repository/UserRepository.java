package com.jutjubiccorps.jutjubic.repository;

import com.jutjubiccorps.jutjubic.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;

public interface UserRepository extends JpaRepository<User, Long> {
    User findOneByUsername(String username);
    User findOneById(Long id);
}
