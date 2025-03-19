package com.RDS.skilltree.viewmodels;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserViewModel {
    private String id;
    private String name;

    public static UserViewModel toViewModel(RdsUserViewModel user) {
        if (user == null) {
            return null;
        }

        return UserViewModel.builder()
                .id(user.getId())
                .name(String.format("%s %s", user.getFirst_name(), user.getLast_name()))
                .build();
    }
}
