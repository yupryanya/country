package guru.qa.country.data.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import guru.qa.country.data.CountryEntity;
import guru.qa.country.domain.graphql.CountryGql;
import guru.qa.country.domain.graphql.CountryInputGql;
import org.springframework.stereotype.Component;

@Component
public class CountryGqlMapper {

  private final ObjectMapper objectMapper;

  public CountryGqlMapper(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  public CountryGql fromEntity(CountryEntity entity) {
    String coordinatesJson = "";
    if (entity.getCoordinates() != null) {
      coordinatesJson = entity.getCoordinates().toString(); // compact JSON
    }

    return new CountryGql(
        entity.getId(),
        entity.getCountryName(),
        entity.getIsoCode(),
        coordinatesJson
    );
  }

  public CountryEntity toEntity(CountryInputGql input) {
    CountryEntity entity = new CountryEntity();
    entity.setCountryName(input.countryName());
    entity.setIsoCode(input.isoCode());
    entity.setCoordinates(parseCoordinates(input.coordinates()));
    return entity;
  }

  public JsonNode parseCoordinates(String coordinates) {
    if (coordinates == null || coordinates.isBlank()) {
      return null;
    }
    try {
      return objectMapper.readTree(coordinates);
    } catch (Exception e) {
      throw new IllegalArgumentException("Failed to parse 'coordinates' JSON: " + coordinates, e);
    }
  }
}