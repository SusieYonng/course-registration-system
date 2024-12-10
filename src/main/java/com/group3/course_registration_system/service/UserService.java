package com.group3.course_registration_system.service;

import com.group3.course_registration_system.entity.User;
import com.group3.course_registration_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Fetch user from the database
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        User user = userOptional.get();
        System.out.println("Loaded user: " + user.getUsername());
        System.out.println("Password (hashed): " + user.getPassword());
        
        // Return a Spring Security UserDetails object
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword()) // Ensure password is encrypted in the database
                .authorities(getAuthorities(user)) // Assign authorities based on role
                .build();
    }

    private GrantedAuthority getAuthorities(User user) {
        // Map user role to a granted authority with the prefix "ROLE_"
        return new SimpleGrantedAuthority("ROLE_" + user.getRole().name());
    }
}
