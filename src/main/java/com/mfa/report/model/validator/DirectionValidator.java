package com.mfa.report.model.validator;

import com.mfa.report.model.Direction;
import com.mfa.report.model.User;
import com.mfa.report.repository.DirectionRepository;
import com.mfa.report.repository.exception.BadRequestException;
import com.mfa.report.repository.exception.ForbiddenException;
import com.mfa.report.service.DirectionService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class DirectionValidator implements Consumer<Direction> {
    private final DirectionRepository repository;

    public void accept(List<Direction> directionList){
        directionList.forEach(this::accept);
    }

    @Override
    public void accept(Direction direction) {
     Set<String> violationsMessage = new HashSet<>();

    }


    public void acceptUser(Direction direction,String userId){
        boolean isResponsible = direction.getResponsible().stream()
                .anyMatch(user -> user.getId().equals(userId));
        if(!isResponsible){
            throw new ForbiddenException("The responsible of one direction cannot post mission to other direction");
        }
    }
}
