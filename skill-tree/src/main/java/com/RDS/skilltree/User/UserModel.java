package com.RDS.skilltree.User;

import com.RDS.skilltree.Skill.SkillModel;
import com.RDS.skilltree.utils.TrackedProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@JsonSerialize
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Users")
public abstract class UserModel implements UserDetails  {
    @Id
    @GeneratedValue
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "rds_user_id", unique = true)
    private String rdsUserId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "image_url", nullable = false)
    private URL imageUrl;

    @Column(name = "user_role", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRole role = UserRole.USER;

    @JsonManagedReference
    @ManyToMany(targetEntity = SkillModel.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_skill", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "skill_id"))
    private Set<SkillModel> skills;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return firstName +" "+ lastName;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
