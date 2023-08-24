package com.RDS.skilltree.User;

import com.RDS.skilltree.Skill.SkillModel;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.net.URL;
import java.util.Set;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
@JsonSerialize
@Table(name = "Users")
public class UserModel {
    @Id
    @GeneratedValue
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "first_name" , nullable = false)
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "image_url", nullable = false)
    private URL imageUrl;

    @Column(name = "user_type", nullable = false)
    private UserType type;

    @Column(name = "user_role", nullable = false)
    private UserRole role;

    @JsonManagedReference
    @ManyToMany(targetEntity = SkillModel.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_skill", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "skill_id"))
    private Set<SkillModel> skills;

    public UserModel(String firstName, String lastName, URL imageUrl, UserType type, UserRole role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.imageUrl = imageUrl;
        this.type = type;
        this.role = role;
    }
}
