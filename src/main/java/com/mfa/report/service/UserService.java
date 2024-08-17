package com.mfa.report.service;

import com.mfa.report.model.User;

public interface UserService {
    User findByUsername(String name);
    User saveUser(User user);
}
