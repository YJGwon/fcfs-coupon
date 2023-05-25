package com.coupop.fcfscoupon.common.exception;

import java.net.URI;
import org.springframework.http.HttpStatus;

public abstract class ApiException extends RuntimeException {

    private static final String TYPE_BLANK = "about:blank";

    private final URI type;
    private final String title;
    private final HttpStatus httpStatus;

    public ApiException(final String type,
                        final String title,
                        final String detail,
                        final HttpStatus httpStatus) {
        super(detail);
        this.type = URI.create(type);
        this.title = title;
        this.httpStatus = httpStatus;
    }

    public ApiException(final String title,
                        final String detail,
                        final HttpStatus httpStatus) {
        this(TYPE_BLANK, title, detail, httpStatus);
    }

    public URI getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
