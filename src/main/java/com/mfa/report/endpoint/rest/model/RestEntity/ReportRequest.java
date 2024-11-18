package com.mfa.report.endpoint.rest.model.RestEntity;

import com.mfa.report.endpoint.rest.model.DTO.DirectionNameDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ReportRequest {
    private String id;
    private String description;
    private UserInformation responsible;
    private DirectionNameDTO requesterDirection;
    private DirectionNameDTO targetDirection;
    private LocalDate createdAt;
    private String status;
}
