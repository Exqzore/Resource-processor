package com.exqzore.resourceprocessor.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

@Service
public class ResourceService {
    private static final Logger logger = LoggerFactory.getLogger(ResourceService.class);

    @Value("${resource-service.location.url}")
    private String resourceUrl;

    public File getFileById(Long id) {
        RestTemplate restTemplate = new RestTemplate();
        String url = resourceUrl + "/" + id;
        try {
            byte[] receivedData = restTemplate.getForObject(new URI(url), byte[].class);
            String fileName = System.currentTimeMillis() + "-" + id + ".mp3";
            return fileFromBytes(receivedData, fileName);
        } catch (URISyntaxException exception) {
            logger.error("Invalid url", exception);
        }
        return null;
    }

    private File fileFromBytes(byte[] bytes, String fileName) {
        File file = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(bytes);
        } catch (IOException ignored) {
        }
        return file;
    }

    public void deleteFileById(Long id) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            Map<String, Long> params = new HashMap<>();
            String url = resourceUrl + "?id=" + id;
            restTemplate.delete(url, params);
        } catch (RestClientException exception) {
            logger.error(exception.getMessage(), exception);
        }
    }
}
