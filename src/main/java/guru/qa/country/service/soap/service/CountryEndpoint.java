package guru.qa.country.service.soap.service;

import guru.qa.country.domain.graphql.CountryGql;
import guru.qa.country.domain.graphql.CountryInputGql;
import guru.qa.country.service.graphql.CountryGqlService;
import guru.qa.xml.country.*;
import org.springframework.data.domain.Page;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.List;

import static guru.qa.country.config.AppConfig.SOAP_NAMESPACE;

@Endpoint
public class CountryEndpoint {

  private final CountryGqlService countryGqlService;

  public CountryEndpoint(CountryGqlService countryGqlService) {
    this.countryGqlService = countryGqlService;
  }

  @PayloadRoot(namespace = SOAP_NAMESPACE, localPart = "countryByCodeRequest")
  @ResponsePayload
  public CountryResponse countryByCode(@RequestPayload CountryByCodeRequest request) {
    CountryGql country = countryGqlService.countryByCode(request.getIsoCode());
    return toCountryResponse(country);
  }

  @PayloadRoot(namespace = SOAP_NAMESPACE, localPart = "countryByIdRequest")
  @ResponsePayload
  public CountryResponse countryById(@RequestPayload CountryByIdRequest request) {
    CountryGql country = countryGqlService.countryById(request.getId());
    return toCountryResponse(country);
  }

  @PayloadRoot(namespace = SOAP_NAMESPACE, localPart = "pageRequest")
  @ResponsePayload
  public CountriesResponse allCountries(@RequestPayload PageRequest request) {
    Page<CountryGql> countriesPage = countryGqlService.allCountries(
        org.springframework.data.domain.PageRequest.of(
            request.getPage(),
            request.getSize()
        )
    );

    List<Country> countries = countriesPage.getContent().stream()
        .map(this::toXmlCountry)
        .toList();

    CountriesResponse response = new CountriesResponse();
    response.setTotalPages(countriesPage.getTotalPages());
    response.setTotalElements(countriesPage.getTotalElements());
    response.getCountries().addAll(countries);
    return response;
  }

  @PayloadRoot(namespace = SOAP_NAMESPACE, localPart = "addCountryRequest")
  @ResponsePayload
  public CountryResponse addCountry(@RequestPayload AddCountryRequest request) {
    CountryGql country = countryGqlService.addCountry(
        new CountryInputGql(
            request.getName(),
            request.getIsoCode(),
            request.getCoordinates()
        )
    );
    return toCountryResponse(country);
  }

  private Country toXmlCountry(CountryGql country) {
    Country xmlCountry = new Country();
    xmlCountry.setId(country.id().toString());
    xmlCountry.setIsoCode(country.isoCode());
    xmlCountry.setName(country.countryName());
    xmlCountry.setCoordinates(country.coordinates());
    return xmlCountry;
  }

  private CountryResponse toCountryResponse(CountryGql country) {
    CountryResponse response = new CountryResponse();
    response.setCountry(toXmlCountry(country));
    return response;
  }
}