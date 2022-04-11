package com.exqzore.resourceprocessor.consumer;

import com.exqzore.resourceprocessor.entity.EntityId;
import com.exqzore.resourceprocessor.entity.FileMetadata;
import com.exqzore.resourceprocessor.exception.ParseMP3FileException;
import com.exqzore.resourceprocessor.service.ProcessorService;
import com.exqzore.resourceprocessor.service.ResourceService;
import com.exqzore.resourceprocessor.service.SongService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@AllArgsConstructor
public class Consumer {
    private static final Logger logger = LoggerFactory.getLogger(Consumer.class);

    private ProcessorService processorService;
    private ResourceService resourceService;
    private SongService songService;

    @KafkaListener(containerFactory = "stringByteContainerFactory", topics = "topic")
    public void consume(byte[] data) {
        Long id;
        try {
            id = Long.valueOf(new String(data));
        } catch (NumberFormatException exception) {
            logger.error("Invalid id", exception);
            return;
        }

        File file = resourceService.getFileById(id);
        if (file == null) {
            logger.error("Failed to get the file");
            return;
        }

        FileMetadata fileMetadata;
        try {
            fileMetadata = processorService.parseFileMetadata(file);
        } catch (ParseMP3FileException e) {
            file.delete();
            resourceService.deleteFileById(id);
            return;
        }
        fileMetadata.setResourceId(id);

        EntityId createdSongId = songService.sendFileMetadata(fileMetadata);
        if (createdSongId == null) {
            resourceService.deleteFileById(id);
        }
        file.delete();
    }
}
