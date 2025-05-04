package io.github.ZalaHarshpalsinh.peer_to_peer_lending_platform.repositories;


import io.github.ZalaHarshpalsinh.peer_to_peer_lending_platform.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer>
{
    User findByEmail(String email);
}
