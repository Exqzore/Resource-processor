package com.exqzore.resourceprocessor.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class FileMetadata {
  private String name;
  private String artist;
  private String album;
  private Long length;
  private Integer year;
}
