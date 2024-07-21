package com.RDS.skilltree.services.external;

import com.RDS.skilltree.exceptions.InternalServerErrorException;
import com.RDS.skilltree.viewmodels.RdsUserViewModel;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

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
        return restTemplate.getForObject(url, RdsUserViewModel.class);
    }

    @Override
    public String signIn(String callbackUrl) {
        String url = UriComponentsBuilder
                .fromUriString(rdsBackendBaseUrl)
                .path("/auth/github/login")
                .queryParam("redirectURL", callbackUrl)
                .queryParam("v2", true)
                .toUriString();

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            if (response.getStatusCode().is5xxServerError() || response.getStatusCode().is4xxClientError()) {
                throw new InternalServerErrorException("Something went wrong during authentication");
            }

            return response.getHeaders().getFirst(HttpHeaders.LOCATION);
        } catch (RestClientException error) {
            log.error("Error calling url: {}, Error : {}", url, error.getMessage());
            throw new RuntimeException("Failed to communicate with RDS backend");
        }
    }
}
