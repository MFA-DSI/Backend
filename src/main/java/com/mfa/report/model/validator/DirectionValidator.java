package com.mfa.report.model.validator;

import com.mfa.report.model.Direction;
import com.mfa.report.model.User;
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
public class DirectionValidator implements Consumer<Direction> {

    public void accept(List<Direction> directionList){
        directionList.forEach(this::accept);
    }

    @Override
    public void accept(Direction direction) {
      //  Set<ConstraintViolation<Direction>> violations = validator.validate(direction);

      //  if(!violations.isEmpty()){
      //      String constraintViolation =
       //             violations.stream()
        //                    .map(ConstraintViolation::getMessage)
        //                    .collect(Collectors.joining(".  "));

        //    throw  new BadRequestException(constraintViolation);
       // }
    }
}
