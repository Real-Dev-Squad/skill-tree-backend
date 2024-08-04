package com.RDS.skilltree.services;

import com.RDS.skilltree.dtos.SkillRequestsDto;
import com.RDS.skilltree.enums.UserSkillStatusEnum;
import com.RDS.skilltree.utils.GenericResponse;
import com.RDS.skilltree.viewmodels.CreateSkillViewModel;
import com.RDS.skilltree.viewmodels.SkillViewModel;

import java.util.List;

public interface SkillService {
    List<SkillViewModel> getAll();

    SkillViewModel create(CreateSkillViewModel skill);

    SkillRequestsDto getAllRequests();

    GenericResponse<String> approveRejectSkillRequest(Integer skillId, String endorseId, UserSkillStatusEnum action);
}
