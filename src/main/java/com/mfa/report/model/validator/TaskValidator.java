package com.mfa.report.model.validator;

import com.mfa.report.model.Task;
import com.mfa.report.repository.exception.BadRequestException;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TaskValidator implements Consumer<Task> {

  public void accept(List<Task> taskList) {
    taskList.forEach(this::accept);
  }

  @Override
  public void accept(Task task) {
    //    Set<ConstraintViolation<Task>> violations = validator.validate(task);
    if (Objects.isNull(task.getDescription())) {
      throw new BadRequestException("Un tâche doit avoir une description");
    }
    // if(!violations.isEmpty()){
    //   String constraintMessage =
    //         violations.stream()
    //               .map(ConstraintViolation::getMessage)
    //                  .collect(Collectors.joining(". "));
    // throw new BadRequestException(constraintMessage);
    // }
  }
}
