package guru.qa.country.data.mapper;

import guru.qa.country.data.CountryEntity;
import guru.qa.country.domain.graphql.CountryGql;
import guru.qa.country.domain.graphql.CountryInputGql;
import org.springframework.stereotype.Component;

@Component
public class CountryGqlMapper {

  private final CoordinatesParser coordinatesParser;

  public CountryGqlMapper(CoordinatesParser coordinatesParser) {
    this.coordinatesParser = coordinatesParser;
  }

  public CountryGql fromEntity(CountryEntity entity) {
    return new CountryGql(
        entity.getId(),
        entity.getCountryName(),
        entity.getIsoCode(),
        entity.getCoordinates() != null ? entity.getCoordinates().toString() : ""
    );
  }

  public CountryEntity toEntity(CountryInputGql input) {
    CountryEntity entity = new CountryEntity();
    entity.setCountryName(input.countryName());
    entity.setIsoCode(input.isoCode());
    entity.setCoordinates(coordinatesParser.parse(input.coordinates()));
    return entity;
  }
}