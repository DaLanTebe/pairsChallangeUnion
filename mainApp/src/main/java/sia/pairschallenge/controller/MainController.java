package sia.pairschallenge.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sia.pairschallenge.repository.Product;
import sia.pairschallenge.service.impl.ProductServiceImpl;

import java.util.List;

/**
 * Контроллер для управления продуктами.
 * Предоставляет REST API для создания, получения, обновления и удаления продуктов.
 */
@RestController
@RequestMapping("/api/products")
public class MainController {

    private final ProductServiceImpl productService;

    /**
     * Конструктор для инициализации контроллера с сервисом продуктов.
     *
     * @param productService Сервис для работы с продуктами.
     */
    public MainController(ProductServiceImpl productService) {
        this.productService = productService;
    }

    /**
     * Создает новый продукт.
     *
     * @param product Объект продукта, который нужно создать в бд.
     * @return Ответ с сообщением о создании продукта.
     */
    @PostMapping
    public ResponseEntity<String> createNewProduct(@RequestBody Product product) {
        productService.create(product);
        return ResponseEntity.ok("Product created with id " + product.getId());
    }

    /**
     * Получает продукт по его идентификатору.
     *
     * @param id Идентификатор продукта.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Integer id) {
        Product productFromMainDB = productService.findById(id);

        return ResponseEntity.ok(productFromMainDB);
    }

    /**
     * Получает список всех продуктов с поддержкой пагинации.
     *
     * @param page Номер страницы (по умолчанию 0).
     * @param size Размер страницы (по умолчанию 0).
     * @return Ответ со списком продуктов.
     */
    @GetMapping
    public ResponseEntity<List<Product>> getProducts(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        List<Product> allProducts = productService.findAll(PageRequest.of(page, size));
        return ResponseEntity.ok(allProducts);
    }

    /**
     * Обновляет существующий продукт.
     *
     * @param id Идентификатор продукта, который нужно обновить.
     * @param product Объект продукта с новыми данными.
     * @return Ответ с сообщением об успешном обновлении продукта.
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable Integer id, @RequestBody Product product) {
        productService.update(id, product);
        return ResponseEntity.ok("Product updated with id " + id);
    }

    /**
     * Удаляет продукт по его идентификатору.
     *
     * @param id Идентификатор продукта, который нужно удалить.
     * @return Ответ с сообщением об успешном удалении продукта.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Integer id) {
        productService.deleteById(id);
        return ResponseEntity.ok("Product deleted with id: " + id);
    }
}