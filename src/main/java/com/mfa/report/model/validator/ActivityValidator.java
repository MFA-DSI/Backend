package com.mfa.report.model.validator;

import com.mfa.report.model.Activity;
import java.util.List;
import java.util.function.Consumer;
import org.springframework.stereotype.Component;

@Component
public class ActivityValidator implements Consumer<Activity> {

  public void accept(List<Activity> activityList) {
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

    // throw  new BadRequestException(constraintViolation);
    // }
  }
}
