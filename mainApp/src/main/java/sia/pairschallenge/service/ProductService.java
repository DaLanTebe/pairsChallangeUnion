package sia.pairschallenge.service;

import org.springframework.data.domain.Pageable;
import sia.pairschallenge.repository.Product;

import java.util.List;

public interface ProductService{

    void update(Integer id, Product product);

    Product findById(Integer id);

    void deleteById(Integer id);

    void create(Product product);

    List<Product> findAll(Pageable pageable);
}
