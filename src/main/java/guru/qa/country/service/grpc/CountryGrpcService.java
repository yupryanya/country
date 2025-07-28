package guru.qa.country.service.grpc;

import guru.qa.country.*;
import guru.qa.country.domain.graphql.CountryGql;
import guru.qa.country.domain.graphql.CountryInputGql;
import guru.qa.country.service.graphql.CountryGqlService;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

@Service
public class CountryGrpcService extends CountryServiceGrpc.CountryServiceImplBase {
  private final CountryGqlService countryGqlService;

  public CountryGrpcService(CountryGqlService countryGqlService) {
    this.countryGqlService = countryGqlService;
  }

  @Override
  public void country(IdRequest request, StreamObserver<CountryResponse> responseObserver) {
    final CountryGql country = countryGqlService.countryById(request.getId());

    responseObserver.onNext(
        CountryResponse.newBuilder()
            .setId(country.id().toString())
            .setCountryName(country.countryName())
            .setIsoCode(country.isoCode())
            .setCoordinates(country.coordinates())
            .build()
    );
    responseObserver.onCompleted();
  }

  @Override
  public void countryByCode(IsoCodeRequest request, StreamObserver<CountryResponse> responseObserver) {
    final CountryGql country = countryGqlService.countryByCode(request.getIsoCode());

    responseObserver.onNext(
        CountryResponse.newBuilder()
            .setId(country.id().toString())
            .setCountryName(country.countryName())
            .setIsoCode(country.isoCode())
            .setCoordinates(country.coordinates())
            .build()
    );
    responseObserver.onCompleted();
  }

  @Override
  public void addCountry(CountryRequest request, StreamObserver<CountryResponse> responseObserver) {
    CountryGql country = countryGqlService.addCountry(
        new CountryInputGql(
            request.getCountryName(),
            request.getIsoCode(),
            request.getCoordinates()
        )
    );

    responseObserver.onNext(
        CountryResponse.newBuilder()
            .setId(country.id().toString())
            .setCountryName(country.countryName())
            .setIsoCode(country.isoCode())
            .setCoordinates(country.coordinates())
            .build()
    );
    responseObserver.onCompleted();
  }

  @Override
  public StreamObserver<CountryRequest> addCountries(StreamObserver<AddCountriesResponse> responseObserver) {
    return new StreamObserver<>() {
      int addedCount = 0;

      @Override
      public void onNext(CountryRequest request) {
        countryGqlService.addCountry(
            new CountryInputGql(
                request.getCountryName(),
                request.getIsoCode(),
                request.getCoordinates()
            )
        );
        addedCount++;
      }

      @Override
      public void onError(Throwable t) {
        t.printStackTrace();
      }

      @Override
      public void onCompleted() {
        responseObserver.onNext(
            AddCountriesResponse.newBuilder()
                .setTotalAdded(addedCount)
                .build()
        );
        responseObserver.onCompleted();
      }
    };
  }
}
