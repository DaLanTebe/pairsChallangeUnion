package sia.pairschallenge.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.*;
import sia.pairschallenge.repository.Product;

/**
 * Конфигурация для настройки Redis в качестве кэша.
 * Этот класс настраивает RedisTemplate и CacheManager для работы с Redis.
 */
@Configuration
@EnableCaching
public class RedisConfig {

    /**
     * Создает и настраивает RedisTemplate для работы с объектами типа Product.
     *
     * @param redisConnectionFactory Фабрика соединений Redis.
     * @return Настроенный RedisTemplate для работы с Product.
     */
    @Bean
    RedisTemplate<String, Product> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(mapper);

        RedisTemplate<String, Product> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setValueSerializer(serializer);

        return redisTemplate;
    }

    /**
     * Создает и настраивает CacheManager для работы с Redis.
     *
     * @param redisConnectionFactory Фабрика соединений Redis, нужна redisTemplate для подключения.
     * @return Настроенный CacheManager для работы с Redis.
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(redisTemplate(redisConnectionFactory).getDefaultSerializer()));

        return RedisCacheManager.builder(redisConnectionFactory).cacheDefaults(config).build();
    }
}
