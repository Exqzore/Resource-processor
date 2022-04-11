package com.exqzore.resourceprocessor.service;

import com.exqzore.resourceprocessor.entity.EntityId;
import com.exqzore.resourceprocessor.entity.FileMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class SongService {
    private static final Logger logger = LoggerFactory.getLogger(SongService.class);

    @Value("${song-service.location.url}")
    private String songServiceUrl;

    public EntityId sendFileMetadata(FileMetadata fileMetadata) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<FileMetadata> request = new HttpEntity<>(fileMetadata);

        try {
            return restTemplate.postForObject(songServiceUrl, request, EntityId.class);
        } catch (RestClientException exception) {
            logger.error(exception.getMessage(), exception);
            return null;
        }
    }
}
