package com.coupop.fcfscoupon.domain.fcfs.support;

import java.util.List;
import java.util.function.Consumer;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class TransactionalRedisOperations {

    private final StringRedisTemplate stringRedisTemplate;

    public TransactionalRedisOperations(final StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public RedisSession startSession() {
        return new RedisSession();
    }

    public class RedisSession {
        private Consumer<RedisOperations<String, String>> callback;

        private RedisSession() {
            this.callback = operations -> {
            };
        }

        public RedisSession watch(final String key) {
            callback = callback.andThen(operation -> operation.watch(key));
            return this;
        }

        public RedisSession multi() {
            callback = callback.andThen(RedisOperations::multi);
            return this;
        }

        public RedisSession andThen(Consumer<RedisOperations<String, String>> callback) {
            this.callback = this.callback.andThen(callback);
            return this;
        }

        public List<Object> exec() {
            return stringRedisTemplate.execute(new SessionCallback<>() {
                @Override
                public <K, V> List<Object> execute(final RedisOperations<K, V> operations) throws DataAccessException {
                    callback.accept((RedisOperations<String, String>) operations);
                    return operations.exec();
                }
            });
        }
    }
}
