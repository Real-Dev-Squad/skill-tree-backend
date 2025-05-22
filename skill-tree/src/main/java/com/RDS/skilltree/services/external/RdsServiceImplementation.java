package com.RDS.skilltree.services.external;

import com.RDS.skilltree.dtos.RdsGetUserDetailsResDto;
import com.RDS.skilltree.exceptions.UserNotFoundException;
import com.RDS.skilltree.utils.Constants.ExceptionMessages;
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
    public RdsGetUserDetailsResDto getUserDetails(String id) {
        String url = rdsBackendBaseUrl + "/users?id=" + id;
        try {
            return restTemplate.getForObject(url, RdsGetUserDetailsResDto.class);
        } catch (RestClientException error) {
            log.error("Error calling url {}, error: {}", url, error.getMessage());
            throw new UserNotFoundException(ExceptionMessages.USER_NOT_FOUND);
        }
    }
}
