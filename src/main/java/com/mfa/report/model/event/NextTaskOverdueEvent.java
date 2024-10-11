package com.mfa.report.model.event;

import com.mfa.report.model.Activity;
import com.mfa.report.model.NextTask;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class NextTaskOverdueEvent {
    private NextTask nextTask;
    private Activity activity;
}
