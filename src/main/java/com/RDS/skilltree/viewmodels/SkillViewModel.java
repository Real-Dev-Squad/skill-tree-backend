package com.RDS.skilltree.viewmodels;

import com.RDS.skilltree.enums.SkillTypeEnum;
import com.RDS.skilltree.models.Skill;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class SkillViewModel {
    private Integer id;
    private String name;
    private SkillTypeEnum type = SkillTypeEnum.ATOMIC;

    public static SkillViewModel toViewModel(Skill skill) {
        if (skill == null) {
            return null;
        }

        SkillViewModel viewModel = new SkillViewModel();
        BeanUtils.copyProperties(skill, viewModel);
        return viewModel;
    }
}
