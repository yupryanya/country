package guru.qa.country.service.graphql;

import guru.qa.country.domain.graphql.CountryGql;
import guru.qa.country.domain.graphql.CountryInputGql;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CountryGqlService {
  CountryGql addCountry(CountryInputGql input);

  CountryGql updateCountry(CountryInputGql input);

  CountryGql countryById(String id);

  CountryGql countryByCode(String code);

  Page<CountryGql> allCountries(Pageable pageable);
}
