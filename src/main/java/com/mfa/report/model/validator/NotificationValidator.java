package com.mfa.report.model.validator;

import com.mfa.report.model.Notification;
import com.mfa.report.repository.exception.BadRequestException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;


@Component
@AllArgsConstructor
public class NotificationValidator implements Consumer<Notification> {


    public void accept (List<Notification> notifications){
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
