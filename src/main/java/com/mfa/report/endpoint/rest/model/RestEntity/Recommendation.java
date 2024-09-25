package com.mfa.report.endpoint.rest.model.RestEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Recommendation {
    private String id;
    private String description;
    private Responsible committer;
    private LocalDate commitDatetime;
}
