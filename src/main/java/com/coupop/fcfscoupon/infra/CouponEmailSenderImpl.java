package com.coupop.fcfscoupon.infra;

import com.coupop.fcfscoupon.model.Coupon;
import com.coupop.fcfscoupon.model.CouponEmailSender;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class CouponEmailSenderImpl implements CouponEmailSender {

    private static final String SUBJECT = "쿠폰입니다.";

    private final JavaMailSender mailSender;

    public CouponEmailSenderImpl(final JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void send(final Coupon coupon, final String toAddress) {
        final MimeMessage mimeMessage = mailSender.createMimeMessage();
        final MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        try {
            mimeMessageHelper.setSubject(SUBJECT);
            mimeMessageHelper.setTo(toAddress);
            mimeMessageHelper.setText(coupon.getValue());
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        mailSender.send(mimeMessage);
    }
}
