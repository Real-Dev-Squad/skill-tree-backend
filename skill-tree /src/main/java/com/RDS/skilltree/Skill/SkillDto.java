package com.RDS.skilltree.Skill;


import com.RDS.skilltree.User.UserModel;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SkillDto {
    private UUID id;

    private String name;

    private SkillType type;

    private Instant createdAt;

    private boolean isDeleted;

}
