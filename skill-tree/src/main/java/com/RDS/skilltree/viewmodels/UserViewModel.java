package com.RDS.skilltree.viewmodels;

import com.RDS.skilltree.User.UserModel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class UserViewModel {
    private String Id;
    private String name;

    public static UserViewModel toViewModel(UserModel user) {
        if (user == null) {
            return null;
        }

        UserViewModel viewModel = new UserViewModel();
        BeanUtils.copyProperties(user, viewModel);
        return viewModel;
    }
}
