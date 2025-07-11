package guru.qa.country.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import guru.qa.country.data.CountryEntity;
import jakarta.annotation.Nonnull;

import java.util.Optional;

public record Country(String name,
                      String isoCode,
                      String coordinates) {

  private static ObjectMapper objectMapper = new ObjectMapper();

  public static @Nonnull Country fromEntity(@Nonnull CountryEntity entity) {
    return new Country(
        entity.getCountryName(),
        entity.getIsoCode(),
        Optional.ofNullable(entity.getCoordinates())
            .map(JsonNode::toString)
            .orElse("")
    );
  }

  public static @Nonnull CountryEntity toEntity(@Nonnull Country country) throws JsonProcessingException {
    CountryEntity entity = new CountryEntity();
    entity.setCountryName(country.name());
    entity.setIsoCode(country.isoCode());
    String coordinates = country.coordinates();
    if (coordinates == null || coordinates.isBlank() || "null".equalsIgnoreCase(coordinates)) {
      entity.setCoordinates(null);
    } else {
      entity.setCoordinates(objectMapper.readTree(coordinates));
    }
    return entity;
  }
}
