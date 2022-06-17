package cn.lrl.config;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.Objects;

@Configuration
@EnableCaching
public class RedisConfig {

    private static final StringRedisSerializer STRING_SERIALIZER = new StringRedisSerializer();
    private static final GenericJackson2JsonRedisSerializer JACKSON__SERIALIZER = new GenericJackson2JsonRedisSerializer();
    private static final GenericFastJsonRedisSerializer FASTJSON__SERIALIZER = new GenericFastJsonRedisSerializer();

    /**
     * 解决JSON序列化字符串的乱码问题
     *
     * @param factory
     * @return RedisTemplate<String, Object>
     */
    @Bean(name = "myRedisTemplate")
    public RedisTemplate<String, Object> myRedisTemplate(RedisConnectionFactory factory) {
        // 配置redisTemplate
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        // key序列化
        redisTemplate.setKeySerializer(STRING_SERIALIZER);
        // value序列化
        redisTemplate.setValueSerializer(FASTJSON__SERIALIZER);
        // Hash key序列化
        redisTemplate.setHashKeySerializer(STRING_SERIALIZER);
        // Hash value序列化
        redisTemplate.setHashValueSerializer(FASTJSON__SERIALIZER);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    /**
     * 解决spring注解序列化时乱码问题
     *
     * @param redisConnectionFactory
     * @return CacheManager
     */
    @Bean
    @Primary
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration redisCacheCfg = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofHours(1)) //设置缓存过期时间
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(STRING_SERIALIZER))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(FASTJSON__SERIALIZER));
        return RedisCacheManager.builder(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory))
                .cacheDefaults(redisCacheCfg)
                .build();
    }
//
//    /*
//     * @description Redis缓存的序列化方式使用redisTemplate.getValueSerializer()，不再使用JDK默认的序列化方式
//     * @author xianping
//     * @date 2020/9/25
//     * @param redisTemplate
//     * @return RedisCacheManager
//     **/
//    @Bean
//    public RedisCacheManager redisCacheManager(RedisTemplate<String, Object> myRedisTemplate) {
//        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(Objects.requireNonNull(myRedisTemplate.getConnectionFactory()));
//        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
//                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(STRING_SERIALIZER))
//                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(FASTJSON__SERIALIZER))
//                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(myRedisTemplate.getValueSerializer()));
//        return new CustomRedisCacheManager(redisCacheWriter, redisCacheConfiguration);
//    }
}
