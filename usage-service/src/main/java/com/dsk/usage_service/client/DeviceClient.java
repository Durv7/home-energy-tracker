package com.dsk.usage_service.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class DeviceClient {

    private final RestTemplate restTemplate;

    private final String baseUrl;

    public DeviceClient(@Value("${device.service.url}") String baseUrl){
        restTemplate = new RestTemplate();
        this.baseUrl = baseUrl;
    }

    public long getUserIdByDeviceId(long deviceId){
        String url = UriComponentsBuilder
                .fromUriString(baseUrl)
                .path("/{deviceId}/user")
                .buildAndExpand(deviceId)
                .toUriString();

        ResponseEntity<Long> response =  restTemplate.getForEntity(url,Long.class);

        return response.getBody();
    }

}
