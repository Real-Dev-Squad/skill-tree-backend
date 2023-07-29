package com.RDS.skilltree;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public final class Skill { // TODO: to be updated according to the database
    @Id
    private int id;
    private String name;
    private String type;
}
