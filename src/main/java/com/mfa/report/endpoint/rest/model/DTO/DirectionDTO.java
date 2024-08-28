package com.mfa.report.endpoint.rest.model.DTO;


import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class DirectionDTO {
    private String id;
    private String name;
    private List<UserDTO> responsible;
}
