package com.mfa.report.model.validator;

import com.mfa.report.model.User;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import com.mfa.report.repository.exception.BadRequestException;
import org.springframework.stereotype.Component;

@Component
public class UserValidator implements Consumer<User> {

  @Override
  public void accept(User user) {

    if ((user.getEmail() == null || !isValidEmail(user.getEmail()))
            && (user.getPhoneNumbers() == null || user.getPhoneNumbers().isEmpty())) {
      throw new BadRequestException("Either a valid email or at least one valid phone number must be provided.");
    }


    if (user.getPhoneNumbers() != null && !user.getPhoneNumbers().isEmpty()) {
      validatePhoneNumbers(user.getPhoneNumbers());
    }


  }
  public boolean isValidEmail(String email) {
    return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
  }


  public void validatePhoneNumbers(String phoneNumbers) {
    String[] phones = phoneNumbers.split(";");
    for (String phone : phones) {
      if (!isValidPhoneNumber(phone.trim())) {
        throw new BadRequestException("Invalid phone number: " + phone);
      }
    }
  }


  public boolean isValidPhoneNumber(String phoneNumber) {
    return phoneNumber != null && phoneNumber.matches("^[+]?[0-9]{7,15}$");
  }


  public void accept(List<User> users) {
    users.forEach(this::accept);
  }
}


