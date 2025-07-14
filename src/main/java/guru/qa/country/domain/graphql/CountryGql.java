package guru.qa.country.domain.graphql;

import java.util.UUID;

public record CountryGql(
    UUID id,
    String countryName,
    String isoCode,
    String coordinates
) {
}