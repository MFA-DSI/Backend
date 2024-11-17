package com.mfa.report.model.event;

import com.mfa.report.model.Direction;
import com.mfa.report.model.ReportRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Data
public class ReportRequestEvent {
    private ReportRequest reportRequest;
    private String type;
}
