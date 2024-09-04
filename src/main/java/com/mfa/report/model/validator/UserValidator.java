package com.mfa.report.model.validator;

import com.mfa.report.model.User;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import com.mfa.report.repository.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserValidator implements Consumer<User> {

  public void accept(List<User> users) {
    users.forEach(this::accept);
  }


  @Override
  public void accept(User user) {
    //   Set<ConstraintViolation<User>> violations = validator.validate(user);
    //  if(!violations.isEmpty()){
    //    String constraintsMessage=
    //          violations.stream()
    //                .map(ConstraintViolation::getMessage)
    //              .collect(Collectors.joining(". "));

    // throw new BadRequestException(constraintsMessage);
    // }
  }
}
