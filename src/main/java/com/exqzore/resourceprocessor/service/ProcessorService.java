package com.exqzore.resourceprocessor.service;

import com.exqzore.resourceprocessor.entity.FileMetadata;
import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.Mp3File;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class ProcessorService {

  public FileMetadata parseFileMetadata(File file) {
    try {
      Mp3File mp3file = new Mp3File(file);
      FileMetadata metadata = new FileMetadata();
      if (mp3file.hasId3v1Tag()) {
        ID3v1 id3v1 = mp3file.getId3v1Tag();
        metadata
            .setAlbum(id3v1.getAlbum())
            .setArtist(id3v1.getArtist())
            .setYear(Integer.valueOf(id3v1.getYear()))
            .setName(id3v1.getTitle());
      }
      metadata.setLength(mp3file.getLength());
      return metadata;
    } catch (Exception ignore) {

    }
    return null;
  }
}
