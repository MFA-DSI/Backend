package com.mfa.report.model.validator;

import com.mfa.report.model.Activity;
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
public class ActivityValidator implements Consumer<Activity> {


    public void accept(List<Activity> activityList){
        activityList.forEach(this::accept);
    }
    @Override
    public void accept(Activity activity) {
      //  Set<ConstraintViolation<Activity>> violations = validator.validate(activity);

      //  if(!violations.isEmpty()){
       //     String constraintViolation =
         //           violations.stream()
           //                 .map(ConstraintViolation::getMessage)
             //               .collect(Collectors.joining(".  "));

            //throw  new BadRequestException(constraintViolation);
       // }
    }
}
