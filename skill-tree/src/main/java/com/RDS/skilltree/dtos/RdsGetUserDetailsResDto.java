package com.RDS.skilltree.dtos;

import com.RDS.skilltree.viewmodels.RdsUserViewModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RdsGetUserDetailsResDto {
    private String message;
    private RdsUserViewModel user;
}
