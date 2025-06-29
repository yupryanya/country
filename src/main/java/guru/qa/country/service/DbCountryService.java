package guru.qa.country.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import guru.qa.country.data.CountryEntity;
import guru.qa.country.data.CountryRepository;
import guru.qa.country.domain.Country;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DbCountryService implements CountryService {

  private final CountryRepository countryRepository;
  private final ObjectMapper objectMapper;

  public DbCountryService(CountryRepository countryRepository, ObjectMapper objectMapper) {
    this.countryRepository = countryRepository;
    this.objectMapper = objectMapper;
  }

  @Override
  public @Nonnull List<Country> allCountries() {
    return countryRepository.findAll().stream()
        .map(this::toDomain)
        .toList();
  }

  @Transactional
  @Override
  public Optional<Country> updateCountryName(@Nonnull Country country) {
    if (country.isoCode() == null || country.name() == null) {
      throw new IllegalArgumentException("ISO code and name must not be null");
    }
    return countryRepository.findByIsoCode(country.isoCode())
        .map(entity -> {
          entity.setCountryName(country.name());
          return toDomain(entity);
        });
  }

  @Override
  public Optional<Country> addCountry(@Nonnull Country country) {
    if (country.isoCode() == null || country.name() == null) {
      throw new IllegalArgumentException("ISO code and name must not be null");
    }
    try {
      CountryEntity entity = toEntity(country);
      CountryEntity saved = countryRepository.save(entity);
      return Optional.of(toDomain(saved));
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException("Invalid coordinates JSON: " + country.coordinates(), e);
    }
  }

  private @Nonnull Country toDomain(@Nonnull CountryEntity entity) {
    return new Country(
        entity.getCountryName(),
        entity.getIsoCode(),
        Optional.ofNullable(entity.getCoordinates())
            .map(JsonNode::toString)
            .orElse("")
    );
  }

  private @Nonnull CountryEntity toEntity(@Nonnull Country country) throws JsonProcessingException {
    CountryEntity entity = new CountryEntity();
    entity.setCountryName(country.name());
    entity.setIsoCode(country.isoCode());
    String coordinates = country.coordinates();
    if (coordinates == null || coordinates.isBlank() || "null".equalsIgnoreCase(coordinates)) {
      entity.setCoordinates(null);
    } else {
      entity.setCoordinates(objectMapper.readTree(coordinates));
    }    return entity;
  }
}