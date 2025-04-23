package sia.pairschallenge.kafka;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

/**
 * Конфигурация для настройки Kafka и создания топиков.
 * Этот класс настраивает KafkaAdmin и создает топик для обработки событий продуктов.
 */
@Configuration
public class KafkaTopicConfig {

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    /**
     * Создает и настраивает KafkaAdmin для управления Kafka.
     *
     * @return Настроенный KafkaAdmin.
     */
    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }

    /**
     * Создает новый топик для обработки событий продуктов.
     * Топик будет иметь 3 партиции и 1 реплику.
     *
     * @return Новый топик с именем "product-events".
     */
    @Bean
    public NewTopic myTopic() {
        return new NewTopic("product-events", 3, (short) 1); // 3 партиции, 1 реплика
    }
}
