package sia.pairschallenge.kafka;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import sia.productevent.event.ProductEvent;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.Map;

/**
 * Конфигурация для настройки Kafka Producer.
 * Этот класс настраивает ProducerFactory и KafkaTemplate для отправки сообщений в Kafka.
 */
@Configuration
public class KafkaProducerConfig {

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    /**
     * Создает и настраивает ProducerFactory для отправки сообщений в Kafka.
     *
     * @return Настроенный ProducerFactory для работы с ProductEvent.
     */
    @Bean
    public ProducerFactory<String, ProductEvent> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    /**
     * Создает и настраивает KafkaTemplate для отправки сообщений в Kafka.
     *
     * @return Настроенный KafkaTemplate для работы с ProductEvent.
     */
    @Bean
    public KafkaTemplate<String, ProductEvent> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    /**
     * Конфигурация параметров для ProducerFactory.
     *
     * @return Карта с параметрами конфигурации для Kafka Producer.
     */
    private Map<String, Object> producerConfigs() {
        return Map.of(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress,
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class
        );
    }
}