package com.mfa.report.model.validator;

import com.mfa.report.model.Recommendation;
import com.mfa.report.model.User;
import com.mfa.report.repository.exception.BadRequestException;
import com.mfa.report.service.UserService;
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
public class RecommendationValidator implements Consumer<Recommendation> {


    private final UserService userService;



    public void accept(Recommendation recommendation,String committerId){
        User user = userService.getUserById(committerId);
        if(!recommendation.getActivity().getMission().getDirection().getResponsible().contains(user) && recommendation.isApproved()){
            throw new BadRequestException("Une recommandation doit être approver par une responsabe de la direction pour être afficher");
        }
    }
    @Override
    public void accept(Recommendation recommendation) {
    //    Set<ConstraintViolation<Recommendation>> violations = validator.validate(recommendation);

      //  if(!violations.isEmpty()){
       //     String constraintViolation =
        //            violations.stream()
       //                     .map(ConstraintViolation::getMessage)
        //                    .collect(Collectors.joining(".  "));

          //  throw  new BadRequestException(constraintViolation);
       // }
    }
}
