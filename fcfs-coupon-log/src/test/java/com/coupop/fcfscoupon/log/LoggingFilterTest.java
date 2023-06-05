package com.coupop.fcfscoupon.log;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import com.coupop.fcfscoupon.log.TestController.Response;
import jakarta.servlet.ServletException;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoggingFilterTest {

    @LocalServerPort
    private int port;

    @SpyBean
    private LoggingFilter loggingFilter;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("Http 요청 처리 시 loggingFilter가 동작한다.")
    void loggingFilterInvoked_whenProcessingRequest() throws ServletException, IOException {
        restTemplate.getForEntity(String.format("http://localhost:%d/get", port), Response.class);
        verify(loggingFilter)
                .doFilterInternal(any(), any(), any());
    }
}
