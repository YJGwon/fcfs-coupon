package com.coupop.fcfscoupon.config;

import jakarta.annotation.PreDestroy;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.embedded.RedisServer;


@Component
public class EmbeddedRedisStarter {

    private final RedisServer redisServer;
    private final Logger logger = LoggerFactory.getLogger(EmbeddedRedisStarter.class);

    public EmbeddedRedisStarter(@Value("${spring.data.redis.port}") final int port) throws IOException {
        if (isAvailable(port)) {
            this.redisServer = new RedisServer(port);
            redisServer.start();
            logger.atInfo().log("Embedded Redis Started");
        } else {
            logger.atInfo().log("Port Not Available, Embedded Redis Already Started");
            redisServer = null;
        }
    }

    @PreDestroy
    public void stopRedis() {
        if (redisServer != null) {
            redisServer.stop();
            logger.atInfo().log("Embedded Redis Stopped");
        }
    }

    private boolean isAvailable(final int port) throws IOException {
        final Process process = findPortAtCmd(port);
        return isEmpty(process);
    }

    private Process findPortAtCmd(final int port) throws IOException {
        final String command = String.format("cmd /c netstat -ano | findstr %d", port);
        return Runtime.getRuntime().exec(command);
    }

    private boolean isEmpty(Process process) {
        String line;
        StringBuilder pidInfo = new StringBuilder();

        try (BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            while ((line = input.readLine()) != null) {
                pidInfo.append(line);
            }
        } catch (Exception e) {
        }

        return pidInfo.toString().isBlank();
    }
}
