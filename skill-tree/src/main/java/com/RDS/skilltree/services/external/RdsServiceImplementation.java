package com.RDS.skilltree.services.external;

import com.RDS.skilltree.viewmodels.RdsUserViewModel;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class RdsServiceImplementation implements RdsService {
    private static final Logger log = LoggerFactory.getLogger(RdsServiceImplementation.class);
    private final RestTemplate restTemplate;

    @Value("${rds.backendBaseUrl}")
    private String rdsBackendBaseUrl;

    @Override
    public RdsUserViewModel getUserDetails(String id) {
        String url = rdsBackendBaseUrl + "/users?id=" + id;

        try {
            return restTemplate.getForObject(url, RdsUserViewModel.class);
        } catch (RestClientException error) {
            log.error("Error calling url: {}, Error : {}", url, error.getMessage());
            throw new RuntimeException("Failed to communicate with RDS backend");
        }
    }
}
