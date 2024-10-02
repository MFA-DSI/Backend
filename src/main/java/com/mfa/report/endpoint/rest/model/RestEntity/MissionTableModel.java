package com.mfa.report.endpoint.rest.model.RestEntity;

import com.mfa.report.model.Mission;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class MissionTableModel {
    private String title;
    private Mission mission;
    private List<String> headers;
}
