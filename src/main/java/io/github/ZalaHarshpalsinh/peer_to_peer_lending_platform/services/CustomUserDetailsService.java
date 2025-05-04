package io.github.ZalaHarshpalsinh.peer_to_peer_lending_platform.services;


import io.github.ZalaHarshpalsinh.peer_to_peer_lending_platform.configs.SecurityUser;
import io.github.ZalaHarshpalsinh.peer_to_peer_lending_platform.entities.User;
import io.github.ZalaHarshpalsinh.peer_to_peer_lending_platform.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// service for letting spring access my custom user
@Service
public class CustomUserDetailsService implements UserDetailsService
{

    private final UserRepository userRepository;

    // DI
    public CustomUserDetailsService(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    // method implementation, in our case, email can be considered username
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        User user = userRepository.findByEmail(username);

        // checking if not found
        if(user == null)
        {
            throw new UsernameNotFoundException("User "+ username + " not found");
        }

        return new SecurityUser(user);
    }
}
