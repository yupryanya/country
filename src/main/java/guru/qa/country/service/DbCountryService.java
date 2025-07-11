package guru.qa.country.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import guru.qa.country.data.CountryEntity;
import guru.qa.country.data.CountryRepository;
import guru.qa.country.domain.Country;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static guru.qa.country.domain.Country.fromEntity;
import static guru.qa.country.domain.Country.toEntity;

@Service
public class DbCountryService implements CountryService {

  private final CountryRepository countryRepository;

  public DbCountryService(CountryRepository countryRepository, ObjectMapper objectMapper) {
    this.countryRepository = countryRepository;
  }

  @Override
  public @Nonnull List<Country> allCountries() {
    return countryRepository.findAll().stream()
        .map(Country::fromEntity)
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
          return fromEntity(entity);
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
      return Optional.of(fromEntity(saved));
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException("Invalid coordinates JSON: " + country.coordinates(), e);
    }
  }
}