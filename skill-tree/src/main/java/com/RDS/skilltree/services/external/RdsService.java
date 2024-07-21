package com.RDS.skilltree.services.external;

import com.RDS.skilltree.viewmodels.RdsUserViewModel;

public interface RdsService {
    RdsUserViewModel getUserDetails(String id);

    String signIn(String callbackUrl);
}
