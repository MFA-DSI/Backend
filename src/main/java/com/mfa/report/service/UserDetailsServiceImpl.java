package com.mfa.report.service;

import com.mfa.report.endpoint.rest.model.Principal;
import com.mfa.report.model.User;
import java.util.Objects;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private final UserService userService;


    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
        User user = userService.getUserByUserMail(mail);
        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException("User of name: " + mail + " not found");
        }
        return Principal.builder().user(user).build();
    }
    }
