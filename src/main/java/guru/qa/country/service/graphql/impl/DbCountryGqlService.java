package guru.qa.country.service.graphql.impl;

import guru.qa.country.data.CountryEntity;
import guru.qa.country.data.CountryRepository;
import guru.qa.country.data.mapper.CountryGqlMapper;
import guru.qa.country.domain.graphql.CountryGql;
import guru.qa.country.domain.graphql.CountryInputGql;
import guru.qa.country.service.graphql.CountryGqlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import guru.qa.country.data.mapper.CoordinatesParser;

import java.util.UUID;

@Service
public class DbCountryGqlService implements CountryGqlService {

  private final CountryRepository countryRepository;
  private final CountryGqlMapper mapper;
  private final CoordinatesParser coordinatesParser;

  @Autowired
  public DbCountryGqlService(CountryRepository countryRepository,
                             CountryGqlMapper mapper,
                             CoordinatesParser coordinatesParser
  ) {
    this.countryRepository = countryRepository;
    this.mapper = mapper;
    this.coordinatesParser = coordinatesParser;
  }

  @Override
  public CountryGql addCountry(CountryInputGql input) {
    CountryEntity entity = mapper.toEntity(input);
    return mapper.fromEntity(countryRepository.save(entity));
  }

  @Override
  public CountryGql updateCountry(CountryInputGql input) {
    CountryEntity existingEntity = countryRepository.findByIsoCode(input.isoCode())
        .map(country -> {
          country.setCountryName(input.countryName());
          country.setCoordinates(coordinatesParser.parse(input.coordinates()));
          return country;
        })
        .orElseThrow(() -> new IllegalArgumentException("Country not found with ISO code: " + input.isoCode()));
    return mapper.fromEntity(countryRepository.save(existingEntity));
  }

  @Override
  public CountryGql countryById(String id) {
    UUID uuid = UUID.fromString(id);
    return countryRepository.findById(uuid)
        .map(mapper::fromEntity)
        .orElseThrow(() -> new IllegalArgumentException("Country not found with id: " + id));
  }

  @Override
  public Page<CountryGql> allCountries(Pageable pageable) {
    return countryRepository.findAll(pageable)
        .map(mapper::fromEntity);
  }
}