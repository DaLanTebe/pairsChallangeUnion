package sia.kafkaconsumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import sia.productevent.event.ProductEvent;

/**
 * Класс Consumer, который слушает сообщения из Kafka-топика "product-events" и логирует их.
 * Этот класс использует аннотацию @KafkaListener для определения топика и группы для потребления сообщений.
 */
@Service
public class Consumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(Consumer.class);

    /**
     * Слушает новые сообщения о событиях продуктов в Kafka-топике "product-events".
     *
     * @param message сообщение типа ProductEvent, полученное из Kafka-топика.
     */
    @KafkaListener(topics = "product-events", groupId = "product-events-listener")
    public void listenNewEmployee(ProductEvent message) {
        LOGGER.info("Сообщение получено: " + message.toString());
    }
}