package com.mfa.report.service;

import com.mfa.report.model.enumerated.Role;
import com.mfa.report.endpoint.rest.model.UserToUpdate;
import com.mfa.report.model.User;
import com.mfa.report.repository.UserRepository;
import com.mfa.report.repository.exception.NotFoundException;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
@Slf4j
public class UserService {
  private final UserRepository userRepository;

  public List<User> crupdateUser(List<User> toCrupdate) {
    return userRepository.saveAll(toCrupdate);
  }


  public User crupdateUser(User toCrupdate) {
    return userRepository.save(toCrupdate);
  }

  public User getUserByUserMail(String mail) {
    return userRepository.findByEmail(mail);
  }

  public User getUserById(String id) {
    return userRepository
        .findById(id)
        .orElseThrow(() -> new NotFoundException("User with id." + id + " not found "));
  }

  public User ModifyUserRole(String id, String role) {
    User user = getUserById(id);
    user.setRole(Role.valueOf(role));
    return userRepository.save(user);
  }

  public User modifyUserInformation(UserToUpdate user) {
    User user1 = userRepository.getById(user.getId());
    user1.setFirstname(user.getFirstname());
    user1.setLastname(user.getLastname());
    user1.setEmail(user.getMail());
    return userRepository.save(user1);
  }


  public List<User> getAllUser(){
    return userRepository.findAll();
  }

}
