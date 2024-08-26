package com.mfa.report.model.validator;

import com.mfa.report.endpoint.rest.model.DTO.MissionDTO;
import com.mfa.report.repository.exception.BadRequestException;
import com.mfa.report.model.Mission;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

@Component
public class MissionValidator implements Consumer<Mission> {

    public void accept (List<Mission> missionList){
        missionList.forEach(this::accept);
    }

    @Override
    public void accept(Mission mission) {
        if(mission.getDescription().isEmpty()){
            throw new BadRequestException("Une mission doit avoir une designation");
        }
    }

    public void accept(MissionDTO missionDTO,Mission mission){
        if (!Objects.equals(mission.getDirection().getId(), missionDTO.getDirection().getId())){
            throw new BadRequestException("La mission d'une direction ne doit pas être soumise par une autre direction");
        }
    }


}
