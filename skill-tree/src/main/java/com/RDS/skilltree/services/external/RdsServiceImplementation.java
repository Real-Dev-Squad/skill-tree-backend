package com.RDS.skilltree.services.external;

import com.RDS.skilltree.viewmodels.RdsUserViewModel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class RdsServiceImplementation implements RdsService {
    private final RestTemplate restTemplate;

    @Value("${rds.backendBaseUrl}")
    private String rdsBackendBaseUrl;

    @Override
    public RdsUserViewModel getUserDetails(String id) {
        String url = rdsBackendBaseUrl + "/users?id=" + id;
        return restTemplate.getForObject(url, RdsUserViewModel.class);
    }
}
