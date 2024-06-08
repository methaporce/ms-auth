package com.metaphorce.auth.respositories;

import com.metaphorce.databaseLib.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String>{

    boolean existsByNameOrEmail(String name, String email);


    public Optional<User> findByName(String name);


}
