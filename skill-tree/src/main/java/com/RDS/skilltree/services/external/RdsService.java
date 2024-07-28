package com.RDS.skilltree.services.external;

import com.RDS.skilltree.dtos.RdsGetUserDetailsResDto;

public interface RdsService {
    RdsGetUserDetailsResDto getUserDetails(String id);

    String signIn(String callbackUrl);
}
