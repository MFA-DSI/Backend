package com.mfa.report.model.validator;

import com.mfa.report.model.NextTask;
import com.mfa.report.repository.exception.BadRequestException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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
