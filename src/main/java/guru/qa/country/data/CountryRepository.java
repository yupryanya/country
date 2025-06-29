package guru.qa.country.data;

import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CountryRepository extends JpaRepository<CountryEntity, UUID> {

  Optional<CountryEntity> findByIsoCode(@Nonnull String isoCose);

}
