package com.appviewx.services;

import com.appviewx.model.CustomUserDetails;
import com.appviewx.model.User;
import com.appviewx.repos.primarydb.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        System.out.println(userRepository.getClass().getName());
        Optional<User> user = userRepository.findByUserName(userName);
        //user.orElseThrow(() -> new UsernameNotFoundException("User with name " + userName + " not found"));
        if(user.isPresent()) {
            return new CustomUserDetails(user.get());
        }
        throw new UsernameNotFoundException("User with name " + userName + " not found");
    }
}
