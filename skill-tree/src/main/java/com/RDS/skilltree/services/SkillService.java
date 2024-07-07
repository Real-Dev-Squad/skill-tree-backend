package com.RDS.skilltree.services;

import com.RDS.skilltree.viewmodels.CreateSkillViewModel;
import com.RDS.skilltree.viewmodels.SkillViewModel;
import java.util.List;

public interface SkillService {
    List<SkillViewModel> getAll();

    SkillViewModel create(CreateSkillViewModel skill);
}
