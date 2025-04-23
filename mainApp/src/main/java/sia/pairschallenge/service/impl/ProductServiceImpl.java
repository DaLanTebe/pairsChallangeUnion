package sia.pairschallenge.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import sia.pairschallenge.repository.Product;
import sia.pairschallenge.repository.ProductRepository;
import sia.pairschallenge.service.ProductService;
import sia.productevent.event.ProductEvent;


import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Реализация сервиса для управления продуктами.
 * Предоставляет методы для создания, обновления, удаления и получения продуктов.
 */
@Service
@CacheConfig(cacheNames = "productCache")
public class ProductServiceImpl implements ProductService {

    private static final Logger log = LogManager.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;

    private final KafkaTemplate<String, ProductEvent> kafkaTemplate;

    /**
     * Конструктор для инициализации сервиса с репозиторием продуктов и KafkaTemplate.
     *
     * @param productRepository Репозиторий для работы с продуктами.
     * @param kafkaTemplate Шаблон для отправки сообщений в Kafka.
     */
    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, KafkaTemplate<String, ProductEvent> kafkaTemplate) {
        this.productRepository = productRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Обновляет существующий продукт.
     *
     * @param id Идентификатор продукта, который нужно обновить.
     * @param product Объект продукта с новыми данными.
     * @throws EntityNotFoundException Если продукт с указанным идентификатором не найден.
     */
    @Override
    @CachePut(key = "#product.id")
    public void update(Integer id, Product product) {
        Product productFromMainDB = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        product.setId(id);
        product.setCreatedAt(productFromMainDB.getCreatedAt());

        Product savedProduct = productRepository.save(product);

        kafkaMessage("product updated", savedProduct);
    }

    /**
     * Находит продукт по его идентификатору.
     *
     * @param id Идентификатор продукта.
     * @return Найденный продукт.
     * @throws EntityNotFoundException Если продукт с указанным идентификатором не найден.
     */
    @Override
    @Cacheable(key = "#id")
    public Product findById(Integer id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
    }

    /**
     * Удаляет продукт по его идентификатору.
     *
     * @param id Идентификатор продукта, который нужно удалить.
     * @throws EntityNotFoundException Если продукт с указанным идентификатором не найден.
     */
    @Override
    @CacheEvict(key = "#id")
    public void deleteById(Integer id) {
        Product productForDelete = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        productRepository.deleteById(id);

        kafkaMessage("product deleted", productForDelete);
    }

    /**
     * Создает новый продукт.
     *
     * @param product Объект продукта, который нужно создать.
     */
    @Override
    public void create(Product product) {
        productRepository.save(product);
        kafkaMessage("product created", product);
    }

    /**
     * Находит все продукты с поддержкой пагинации.
     *
     * @param pageable Параметры пагинации.
     * @return Список всех продуктов.
     */
    @Override
    public List<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable).getContent();
    }

    /**
     * Отправляет сообщение в Kafka о событии с продуктом.
     *
     * @param message Сообщение о событии.
     * @param product Продукт, связанный с событием.
     * Преобразует в ProductEvent для последующей десериализации и логировании в сервисе kafkaConsumer
     */
    private void kafkaMessage(String message, Product product) {
        CompletableFuture<SendResult<String, ProductEvent>> send =
                kafkaTemplate.send("product-events", new ProductEvent(message, product.getId(),
                        product.getName(), product.getDescription(),
                        product.getPrice(), product.getQuantity(),
                        product.getUpdatedAt(), product.getCreatedAt()));

        send.whenComplete((result, exception) -> {
            if (exception != null) {
                log.error("message failed to send", exception);
            }
        });
    }
}