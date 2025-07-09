package guru.qa.country.service;

import guru.qa.country.domain.Country;

import java.util.List;
import java.util.Optional;

public interface CountryService {
  List<Country> allCountries();

  Optional<Country> updateCountryName(Country country);

  Optional<Country> addCountry(Country country);
}
