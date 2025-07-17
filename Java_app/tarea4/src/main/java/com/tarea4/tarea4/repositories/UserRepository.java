package com.tarea4.tarea4.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tarea4.tarea4.models.User;

public interface UserRepository extends JpaRepository<User, Long>{
    User findByUsername(String username);
}
