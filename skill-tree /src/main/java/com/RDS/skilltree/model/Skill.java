package com.RDS.skilltree.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public final class Skill { // TODO: to be updated according to the database
    @Id
    private Long id;
    private String name;
    private String type;
}
