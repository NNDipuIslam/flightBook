package com.classz.flightbook.user.service;

import com.classz.flightbook.user.entity.User;
import com.classz.flightbook.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    public Optional<User> getUserById(Long id){
        return userRepository.findById(id);
    }
}
