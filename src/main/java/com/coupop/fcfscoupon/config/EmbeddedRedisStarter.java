package com.coupop.fcfscoupon.config;

import jakarta.annotation.PreDestroy;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
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
        return isEmpty(port, process);
    }

    private Process findPortAtCmd(final int port) throws IOException {
        final String command = String.format("cmd /c netstat -ano | findstr %d", port);
        return Runtime.getRuntime().exec(command);
    }

    private boolean isEmpty(final int port, final Process process) {
        final List<String> pidInfos = new ArrayList<>();

        String line;
        try (BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            while ((line = input.readLine()) != null) {
                pidInfos.add(line);
            }
        } catch (Exception e) {
        }

        return pidInfos.stream()
                .map(str -> str.split("\\s+"))
                .noneMatch(arr -> String.format("127.0.0.1:%d", port).equals(arr[2]));
    }
}
