package com.mfa.report.model.validator;

import com.mfa.report.model.NextTask;
import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class NextTaskValidator implements Consumer<NextTask> {

  /// private  final Validator validator;

  @Override
  public void accept(NextTask nextTask) {
    //  Set<ConstraintViolation<NextTask>> violations = validator.validate(nextTask);

    // if(!violations.isEmpty()){
    //    String constraintMessage =
    //           violations.stream()
    //                  .map(ConstraintViolation::getMessage)
    //                 .collect(Collectors.joining(". "));
    // throw new BadRequestException(constraintMessage);
    // }

  }
}
