package guru.qa.country.controller.graphql;

import guru.qa.country.domain.graphql.CountryGql;
import guru.qa.country.service.graphql.CountryGqlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class CountryQueryController {
  private final CountryGqlService CountryService;

  @Autowired
  public CountryQueryController(CountryGqlService CountryService) {
    this.CountryService = CountryService;
  }

  @QueryMapping
  public Slice<CountryGql> allCountries(@Argument int page, @Argument int size) {
    return CountryService.allCountries(PageRequest.of(page, size));
  }

  @QueryMapping
  public CountryGql country(@Argument String id) {
    return CountryService.countryById(id);
  }
}
