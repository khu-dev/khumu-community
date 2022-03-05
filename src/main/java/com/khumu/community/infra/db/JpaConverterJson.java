package com.khumu.community.infra.db;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;
import java.util.List;

@Converter(autoApply = false)
@RequiredArgsConstructor
// json 필드를 위한 컨버터
public class JpaConverterJson implements AttributeConverter<List<String>, String> {

  private final ObjectMapper objectMapper;

  @Override
  public String convertToDatabaseColumn(List<String> meta) {
    try {
      return objectMapper.writeValueAsString(meta);
    } catch (JsonProcessingException ex) {
      throw new IllegalArgumentException();
    }
  }

  @Override
  public List<String> convertToEntityAttribute(String dbData) {
    try {
      if (dbData == null){
//        throw new IllegalArgumentException();
        return null;
      }
      return objectMapper.readValue(dbData, List.class);
    } catch (IOException ex) {
      // logger.error("Unexpected IOEx decoding json from database: " + dbData);
      throw new IllegalArgumentException();

    }
  }
}