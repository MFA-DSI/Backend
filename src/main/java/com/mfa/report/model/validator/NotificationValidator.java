package com.mfa.report.model.validator;

import com.mfa.report.model.Notification;
import java.util.List;
import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class NotificationValidator implements Consumer<Notification> {

  public void accept(List<Notification> notifications) {
    notifications.forEach(this::accept);
  }

  @Override
  public void accept(Notification notification) {
    // Set<ConstraintViolation<Notification>> violations = validator.validate(notification);
    //  if(!violations.isEmpty()){
    //     String constraintViolation =
    //           violations.stream()
    //                 .map(ConstraintViolation::getMessage)
    //               .collect(Collectors.joining(".  "));

    // throw  new BadRequestException(constraintViolation);
    // }
  }
}
