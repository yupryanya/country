package guru.qa.country.controller;

import guru.qa.country.domain.Country;
import guru.qa.country.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/country")
public class CountryController {
  private final CountryService countryService;

  @Autowired
  public CountryController(CountryService countryService) {
    this.countryService = countryService;
  }

  @GetMapping("/all")
  public List<Country> getAllCountries() {
    return countryService.allCountries();
  }

  @PostMapping("/add")
  public Country addCountry(@RequestBody Country country) {
    return countryService.addCountry(country)
        .orElseThrow(() -> new IllegalArgumentException("Failed to add country: " + country));
  }

  @PatchMapping("/updateName")
  public Country updateCountryName(@RequestBody Country country) {
    return countryService.updateCountryName(country)
        .orElseThrow(() -> new IllegalArgumentException("Failed to update country name for ISO code: " + country.isoCode()));
  }
}
