package com.mfa.report.model.event;


import com.mfa.report.model.Activity;
import com.mfa.report.model.Direction;
import com.mfa.report.model.Recommendation;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RecommendationPostedEvent {
    private Recommendation recommendation;
    private Direction direction;
}
