package com.coupop.fcfscoupon.domain.coupon.study;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import com.coupop.fcfscoupon.domain.coupon.study.types.BugReport;
import com.coupop.fcfscoupon.domain.coupon.study.types.BugReportNotExtends;
import com.coupop.fcfscoupon.domain.coupon.study.types.ModifiedBugReport;
import com.coupop.fcfscoupon.domain.coupon.study.types.Report;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

@Disabled
@SpringBootTest
public class MongoTypeMappingStudyTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @BeforeEach
    void cleanUp() {
        mongoTemplate.dropCollection("report");
    }

    @Test
    @DisplayName("저장했던 type의 supertype에 맵핑하면 저장했던 subtype의 instance로 맵핑된다.")
    void isInstanceOfSubtype_ifMapToSupertype() {
        // given
        mongoTemplate.insert(
                new BugReport("title", "content", "environment"), "report");

        // when
        final Report found = mongoTemplate.findOne(query(where("title").is("title")), Report.class);

        // then
        assertThat(found).isInstanceOf(BugReport.class);
    }

    @Test
    @DisplayName("upcasting한 후 저장해도 instance type으로 저장된다.")
    void isInstanceOfSubtype_ifSavedAsSupertype() {
        // given
        final Report report = new BugReport("title", "content", "environment");
        mongoTemplate.insert(report, "report");

        // when
        final Report found = mongoTemplate.findOne(query(where("title").is("title")), Report.class);

        // then
        assertThat(found).isInstanceOf(BugReport.class);
    }

    @Test
    @DisplayName("저장했던 type이 아니고 추가 필드가 있어도 문서의 필드가 포함되어있으면 맵핑할 수 있다.")
    void canBeMapped_toOtherClassThatContainsFields() {
        // given
        mongoTemplate.insert(
                new BugReport("title", "content", "environment"), "report");

        // when
        final Report found = mongoTemplate.findOne(
                query(where("title").is("title")), ModifiedBugReport.class, "report");

        // then
        assertThat(found).isInstanceOf(ModifiedBugReport.class);
    }

    @Test
    @DisplayName("저장했던 type이 아니고 일부 필드가 존재하지 않아도 맵핑할 수 있다.")
    void test() {
        // given
        mongoTemplate.insert(
                new BugReportNotExtends("title", "content", "environment"), "report");

        // when
        final Report found = mongoTemplate.findOne(
                query(where("title").is("title")), Report.class, "report");

        // then
        assertThat(found).isExactlyInstanceOf(Report.class);
    }
}
