package guru.qa.country.service.graphql;

import guru.qa.country.domain.graphql.CountryGql;
import guru.qa.country.domain.graphql.CountryInputGql;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface CountryGqlService {
  CountryGql addCountry(CountryInputGql input);

  CountryGql updateCountry(CountryInputGql input);

  CountryGql countryById(String id);

  Slice<CountryGql> allCountries(Pageable pageable);
}
