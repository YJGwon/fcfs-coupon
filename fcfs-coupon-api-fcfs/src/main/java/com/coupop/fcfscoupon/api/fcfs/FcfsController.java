package com.coupop.fcfscoupon.api.fcfs;

import com.coupop.fcfscoupon.api.fcfs.dto.HistoryRequest;
import com.coupop.fcfscoupon.api.fcfs.dto.HistoryResponse;
import com.coupop.fcfscoupon.api.fcfs.dto.IssuanceRequest;
import com.coupop.fcfscoupon.api.fcfs.dto.ResendRequest;
import com.coupop.fcfscoupon.api.fcfs.support.RequestTime;
import com.coupop.fcfscoupon.client.coupon.CouponService;
import com.coupop.fcfscoupon.common.exception.ApiException;
import com.coupop.fcfscoupon.domain.fcfs.FcfsIssueService;
import com.coupop.fcfscoupon.domain.history.HistoryService;
import com.coupop.fcfscoupon.domain.history.dto.CouponIssueHistoryRecord;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FcfsController {

    private final FcfsIssueService fcfsIssueService;
    private final HistoryService historyService;
    private final CouponService couponService;
    private final RequestTime requestTime;

    @PostMapping("/issue")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void issue(@RequestBody @Validated final IssuanceRequest request) {
        fcfsIssueService.issue(request.email(), requestTime.getValue());
    }

    @GetMapping("/history")
    @ResponseStatus(HttpStatus.OK)
    public HistoryResponse findHistoryByEmail(@RequestBody @Validated final HistoryRequest request) {
        final List<CouponIssueHistoryRecord> issueHistoryRecords = historyService.findByEmail(request.email());
        return HistoryResponse.of(issueHistoryRecords);
    }

    @PostMapping("/resend")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void resend(@RequestBody final ResendRequest request) {
        couponService.resend(request.historyId());
    }

    @ExceptionHandler(ApiException.class)
    public ErrorResponse handleApiExceptions(final ApiException e) {
        return ErrorResponse.builder(e, e.getHttpStatus(), e.getMessage())
                .type(e.getType())
                .title(e.getTitle())
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleValidationExceptions(final MethodArgumentNotValidException e) {
        final FieldError fieldError = e.getFieldError();
        return ErrorResponse.builder(e, HttpStatus.BAD_REQUEST, fieldError.getRejectedValue().toString())
                .type(e.getBody().getType())
                .title(fieldError.getDefaultMessage())
                .build();
    }

    @ExceptionHandler(Exception.class)
    public ErrorResponse handleServerExceptions(final Exception e) {
        e.printStackTrace();
        return ErrorResponse.create(e, HttpStatus.INTERNAL_SERVER_ERROR, "서버에 오류가 발생했습니다.");
    }
}
