package com.RDS.skilltree.viewmodels;

import com.RDS.skilltree.enums.SkillTypeEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SkillViewModel {
    private Integer id;
    private String name;
    private SkillTypeEnum type = SkillTypeEnum.ATOMIC;
}
