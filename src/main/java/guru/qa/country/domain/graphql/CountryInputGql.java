package guru.qa.country.domain.graphql;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.qa.country.data.CountryEntity;
import lombok.SneakyThrows;

public record CountryInputGql(
    String countryName,
    String isoCode,
    String coordinates
) {
  private static ObjectMapper objectMapper = new ObjectMapper();

  @SneakyThrows
  public static CountryEntity toEntity(CountryInputGql country) {
    CountryEntity entity = new CountryEntity();
    entity.setCountryName(country.countryName());
    entity.setIsoCode(country.isoCode());
    String coordinates = country.coordinates();
    if (coordinates == null || coordinates.isBlank()) {
      entity.setCoordinates(null);
    } else {
      entity.setCoordinates(objectMapper.readTree(coordinates));
    }
    return entity;
  }
}
