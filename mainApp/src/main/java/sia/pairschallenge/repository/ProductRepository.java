package sia.pairschallenge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("postgresRepository")
public interface ProductRepository extends JpaRepository<Product, Integer> {
}
