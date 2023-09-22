package com.RDS.skilltree.utils;

import com.RDS.skilltree.utils.RDSUser.Response;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

@Component
public class FetchAPI {
    private final RestTemplate restTemplate;

    public FetchAPI(RestTemplateBuilder restTemplateBuilder){
        restTemplate = restTemplateBuilder.build();
    }

    @Async
    public CompletableFuture<Response> getRDSUserData(String userId) {
        String url = String.format("https://api.realdevsquad.com/users/userId/%s", userId);
        try{
            ResponseEntity<Response> response = restTemplate.getForEntity(url, Response.class);
            Response result = response.getBody();
            return CompletableFuture.completedFuture(result);
        }catch (Exception e){
            throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
