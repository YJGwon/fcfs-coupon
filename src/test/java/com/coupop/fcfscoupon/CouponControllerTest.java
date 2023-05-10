package com.coupop.fcfscoupon;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.coupop.fcfscoupon.dto.CouponResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest
class CouponControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CouponService couponService;

    @DisplayName("쿠폰 발행에 성공하면 쿠폰 내용과 함께 Created 상태를 반환한다.")
    @Test
    void issue() throws Exception {
        // given
        final String message = "뭔가 좋은 쿠폰";
        final CouponResponse couponResponse = new CouponResponse(message);

        given(couponService.issue())
                .willReturn(couponResponse);

        // when
        final ResultActions resultActions = mockMvc.perform(post("/issue"));

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("value").value(message));
    }
}
