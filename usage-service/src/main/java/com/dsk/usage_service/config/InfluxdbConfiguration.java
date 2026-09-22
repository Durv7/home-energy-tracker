package com.dsk.usage_service.config;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InfluxdbConfiguration {

    @Value("${influxdb.url}")
    private String influxdbURL;
    @Value("${influxdb.token}")
    private String influxdbToken;
    @Value("${influxdb.org}")
    private String influxdbOrg;

    @Bean
    public InfluxDBClient getInfluxDBClient(){
        return InfluxDBClientFactory.create(influxdbURL , influxdbToken.toCharArray() , influxdbOrg);
    }
}
