package com.coupop.fcfscoupon.domain.history;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import com.coupop.fcfscoupon.domain.history.dto.CouponIssueHistoryRecord;
import com.coupop.fcfscoupon.domain.history.exception.HistoryNotFoundException;
import com.coupop.fcfscoupon.domain.history.model.CouponIssueHistory;
import com.coupop.fcfscoupon.domain.history.testconfig.MongoDatabaseCleaner;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

@SpringBootTest
class HistoryServiceTest {

    @Autowired
    private HistoryService historyService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private MongoDatabaseCleaner databaseCleaner;

    @BeforeEach
    void cleanUp() {
        databaseCleaner.clean();
    }

    @Test
    @DisplayName("쿠폰 발급 이력을 저장한다.")
    void create() {
        // given
        final String email = "foo@bar.com";
        final String couponId = "fakeID";

        // when
        historyService.create(email, couponId);

        // then
        final CouponIssueHistory saved = mongoTemplate
                .findOne(query(where("email").is(email)), CouponIssueHistory.class);
        assertThat(saved.getCouponId()).isEqualTo(couponId);
    }

    @DisplayName("이메일로 쿠폰 발급 이력을 조회하여 응답한다.")
    @Test
    void findByEmail() {
        // given
        final String email = "foo@bar.com";
        final String couponId = "fakeID";
        historyService.create(email, couponId);

        // when
        final List<CouponIssueHistoryRecord> historyRecords = historyService.findByEmail(email);

        // then
        assertThat(historyRecords).hasSize(1);
        assertThat(historyRecords.get(0).couponId()).isEqualTo(couponId);
    }

    @DisplayName("이메일로 쿠폰 발급 이력을 조회할때 이력이 존재하지 않으면 예외가 발생한다.")
    @Test
    void findByEmail_ifHistoryNotFound() {
        assertThatExceptionOfType(HistoryNotFoundException.class)
                .isThrownBy(() -> historyService.findByEmail("foo@bar.com"));
    }

    @DisplayName("id로 쿠폰 발급 이력을 조회한다.")
    @Test
    void resend() {
        // given
        final String email = "foo@bar.com";
        final String couponId = "fakeID";
        historyService.create(email, couponId);
        final CouponIssueHistory history = mongoTemplate
                .findOne(query(where("email").is(email)), CouponIssueHistory.class);

        // when
        final CouponIssueHistoryRecord found = historyService.findById(history.getId());

        // then
        assertThat(found.couponId()).isEqualTo(couponId);
    }

    @DisplayName("id로 쿠폰 발급 이력을 조회할 때 이력이 존재하지 않으면 예외가 발생한다.")
    @Test
    void findById_ifHistoryNotFound() {
        assertThatExceptionOfType(HistoryNotFoundException.class)
                .isThrownBy(() -> historyService.findById("invalidId"));
    }
}
