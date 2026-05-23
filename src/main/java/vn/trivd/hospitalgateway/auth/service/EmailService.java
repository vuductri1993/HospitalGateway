package vn.trivd.hospitalgateway.auth.service;

import com.example.hospitalenities.entity.Doctor;
import com.example.hospitalenities.entity.Notification;
import com.example.hospitalenities.entity.Patient;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import vn.trivd.hospitalgateway.service.NotificationService;

@Service
public class EmailService {
    @Autowired
    private NotificationService notificationService;

    public void sendOtp(String to, String otp) {
        /*SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject("Your OTP code");
        msg.setText("Your OTP is: " + otp + "\nIt expires in a few minutes.");
        mailSender.send(msg);*/
    }

    public void sendAccountCreated(Doctor d, Patient p, String email) {
        Notification notification = new Notification();
        if (!ObjectUtils.isEmpty(d)) {
            notification.setDoctorId(d.getDoctorId());
        }
        if (!ObjectUtils.isEmpty(p)) {
            notification.setPatientId(p.getPatientId());
        }
        String body = "Your account has been created.\nLogin email: " + email;
        notification.setBody(body);
        notification.setTitle("Your account has been created");
        notificationService.save(notification);
    }
}

