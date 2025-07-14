package guru.qa.country.domain.graphql;

public record CountryInputGql(
    String countryName,
    String isoCode,
    String coordinates
) {
}
