package com.mfa.report.model.event;

import com.mfa.report.model.Direction;
import com.mfa.report.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserCreatedEvent {
    private User user;
    private Direction direction;
}
