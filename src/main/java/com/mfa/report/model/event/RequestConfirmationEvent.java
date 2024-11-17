package com.mfa.report.model.event;

import com.mfa.report.model.ReportRequest;
import com.mfa.report.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RequestConfirmationEvent {
    private User responsible;
    private ReportRequest reportRequest;
}
