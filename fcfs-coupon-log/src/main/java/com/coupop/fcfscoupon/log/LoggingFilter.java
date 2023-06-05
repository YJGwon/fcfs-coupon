package com.coupop.fcfscoupon.log;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Component
@Slf4j
public class LoggingFilter extends OncePerRequestFilter {

    private static final String HTTP_REQUEST_FORMAT = "\n### HTTP REQUEST ###\nMethod: {}\nURI: {}\n";
    private static final String REQUEST_BODY_FORMAT = "Content-Type: {}\nBody: {}\n";
    private static final String TIME_FORMAT = "\n### Request Processed ###\n{} {} [Time: {} ms]";
    private static final String HTTP_RESPONSE_FORMAT = "\n### HTTP RESPONSE ###\nStatusCode: {}";
    private static final String HTTP_RESPONSE_WITH_BODY_FORMAT = HTTP_RESPONSE_FORMAT + "\nBody: {}";

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {
        final ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        final ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);
        final long startTime = System.currentTimeMillis();

        MDC.put("traceId", UUID.randomUUID().toString());
        filterChain.doFilter(wrappedRequest, wrappedResponse);

        logRequest(wrappedRequest);
        logDuration(wrappedRequest, System.currentTimeMillis() - startTime);
        logResponse(wrappedResponse);
        MDC.clear();
        wrappedResponse.copyBodyToResponse();
    }

    private void logRequest(final ContentCachingRequestWrapper request) {
        final String requestBody = new String(request.getContentAsByteArray());
        log.info(HTTP_REQUEST_FORMAT + REQUEST_BODY_FORMAT,
                request.getRequestURI(),
                request.getMethod(),
                request.getContentType(),
                requestBody);
    }

    private void logDuration(final ContentCachingRequestWrapper request,
                             final long duration) {
        log.info(TIME_FORMAT, request.getMethod(), request.getRequestURI(), duration);
    }

    private void logResponse(final ContentCachingResponseWrapper response) {
        final Optional<String> body = getJsonResponseBody(response);

        if (body.isPresent()) {
            log.info(HTTP_RESPONSE_WITH_BODY_FORMAT, response.getStatus(), body.get());
            return;
        }

        log.info(HTTP_RESPONSE_FORMAT, response.getStatus());
    }

    private Optional<String> getJsonResponseBody(final ContentCachingResponseWrapper response) {
        if (Objects.equals(response.getContentType(), MediaType.APPLICATION_JSON_VALUE)) {
            return Optional.of(new String(response.getContentAsByteArray()));
        }

        return Optional.empty();
    }
}
