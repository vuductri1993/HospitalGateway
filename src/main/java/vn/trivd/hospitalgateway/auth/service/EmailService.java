package vn.trivd.hospitalgateway.auth.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendOtp(String to, String otp) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject("Your OTP code");
        msg.setText("Your OTP is: " + otp + "\nIt expires in a few minutes.");
        mailSender.send(msg);
    }

    public void sendAccountCreated(String to, String email) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject("Account created");
        msg.setText("Your account has been created.\nLogin email: " + email);
        mailSender.send(msg);
    }
}

