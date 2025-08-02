package guru.qa.country.data.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CoordinatesParser {

  @Autowired
  private final ObjectMapper objectMapper;

  public CoordinatesParser(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  public JsonNode parse(String coordinates) {
    if (coordinates == null || coordinates.isBlank()) {
      return null;
    }

    try {
      return objectMapper.readTree(coordinates);
    } catch (Exception e) {
      throw new IllegalArgumentException("Invalid coordinates JSON: " + coordinates, e);
    }
  }
}
