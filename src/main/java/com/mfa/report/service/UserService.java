package com.mfa.report.service;

import com.mfa.report.repository.exception.NotFoundException;
import com.mfa.report.model.User;
import com.mfa.report.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@AllArgsConstructor
@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    public User getUserByUsername(String username){
        return  userRepository.findByUsername(username);
    }

    public List<User> crupdateUser (List<User> toCrupdate){
        return  userRepository.saveAll(toCrupdate);
    }
    public User getUserByUserMail(String mail){
        return userRepository.findByEmail(mail);
    }

    public User getUserById(String id){
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User with id." + id + " not found "));
    }
    public User findByUsername(String userName) {
        return userRepository.findByUsername(userName);
    }
}
