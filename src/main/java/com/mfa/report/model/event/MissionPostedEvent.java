package com.mfa.report.model.event;


import com.mfa.report.model.Direction;
import com.mfa.report.model.Mission;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class MissionPostedEvent {
    private Mission mission;
    private Direction direction;
}
