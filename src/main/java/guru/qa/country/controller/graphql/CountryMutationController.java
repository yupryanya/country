package guru.qa.country.controller.graphql;

import guru.qa.country.domain.graphql.CountryGql;
import guru.qa.country.domain.graphql.CountryInputGql;
import guru.qa.country.service.graphql.CountryGqlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
public class CountryMutationController {
  private final CountryGqlService countryService;

  @Autowired
  public CountryMutationController(CountryGqlService countryService) {
    this.countryService = countryService;
  }

  @MutationMapping
  public CountryGql addCountry(@Argument CountryInputGql input) {
    return countryService.addCountry(input);
  }

  @MutationMapping
  public CountryGql updateCountry(@Argument CountryInputGql input) {
    return countryService.updateCountry(input);
  }
}
