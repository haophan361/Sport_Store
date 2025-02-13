package com.sport_store.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class mail_Service {
    private final JavaMailSender mailSender;

    public void Send_codeVerifyEmail(String email, String forgetPassword_code) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(email);
        helper.setFrom("phanphuchao361@gmail.com");
        helper.setSubject("Reset mật khẩu");
        String content = "<p>Đây là mã để xác minh tài khoản gmail của bạn:</p>" +
                "<h2 style='color: #007bff; text-align: center;'>" + forgetPassword_code + "</h2>" +
                "<p>Mã này sẽ hết hạn sau <strong>5 phút</strong>.</p>";
        helper.setText(content, true);
        mailSender.send(message);
    }

    public void SendEmailRandomPassword(String email, String password) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(email);
        helper.setFrom("phanphuchao361@gmail.com");
        helper.setSubject("Mật khẩu tự động cho tài khoản lần đầu đăng nhập");
        String content = "<p> Mật khẩu của bạn là: "
                + "<strong>" + password + "</strong>"
                + "</p>";
        helper.setText(content, true);
        mailSender.send(message);
    }
}
