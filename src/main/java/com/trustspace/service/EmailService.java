package com.trustspace.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    
    private final JavaMailSender mailSender;
    
    @Value("${app.verification-url}")
    private String verificationUrl;
    
    @Value("${app.reset-password-url}")
    private String resetPasswordUrl;
    
    @Async
    public void sendVerificationEmail(String toEmail, String token) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("Verify Your Email - TrustSpace");
            message.setText("Please click the link below to verify your email:\n\n" +
                    verificationUrl + "?token=" + token + "\n\n" +
                    "This link will expire in 24 hours.\n\n" +
                    "Thank you,\nTrustSpace Team");
            
            mailSender.send(message);
            log.info("Verification email sent to: {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send verification email to: {}", toEmail, e);
        }
    }
    
    @Async
    public void sendPasswordResetEmail(String toEmail, String token) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("Reset Your Password - TrustSpace");
            message.setText("Please click the link below to reset your password:\n\n" +
                    resetPasswordUrl + "?token=" + token + "\n\n" +
                    "This link will expire in 1 hour.\n\n" +
                    "If you didn't request this, please ignore this email.\n\n" +
                    "Thank you,\nTrustSpace Team");
            
            mailSender.send(message);
            log.info("Password reset email sent to: {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send password reset email to: {}", toEmail, e);
        }
    }
    
    @Async
    public void sendBookingNotification(String toEmail, String message) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(toEmail);
            mailMessage.setSubject("Booking Update - TrustSpace");
            mailMessage.setText(message);
            
            mailSender.send(mailMessage);
            log.info("Booking notification sent to: {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send booking notification to: {}", toEmail, e);
        }
    }
}
