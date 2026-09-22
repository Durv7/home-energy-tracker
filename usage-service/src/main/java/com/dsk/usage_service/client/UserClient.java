package com.dsk.usage_service.client;

import com.dsk.usage_service.dto.UserDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class UserClient {

    private RestTemplate restTemplate;
    private String baseUrl;

    public UserClient(@Value("${user.service.url}") String baseUrl){
        this.baseUrl = baseUrl;
        this.restTemplate = new RestTemplate();
    }

    public UserDTO getUser(long userId){
        String url = UriComponentsBuilder
                .fromUriString(baseUrl)
                .path("/{userId}")
                .buildAndExpand(userId)
                .toUriString();

        ResponseEntity<UserDTO> response =  restTemplate.getForEntity(url , UserDTO.class);
        return response.getBody();
    }
}
