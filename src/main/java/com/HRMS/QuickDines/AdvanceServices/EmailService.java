package com.HRMS.QuickDines.AdvanceServices;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;


    public void sendMail(
            String to,
            String subject,
            String body){

        SimpleMailMessage mail =
                new SimpleMailMessage();

        mail.setTo(to);
        mail.setSubject(subject);
        mail.setText(body);

        mailSender.send(mail);

    }


    public void sendRegistrationMail(){}

    public void sendLoginOTP(){}

    public void sendForgotPasswordOTP(){}

    public void sendResetPasswordMail(){}

    public void sendWelcomeMail(){}

    public void sendAccountCreatedMail(){}

    public void sendPasswordChangedMail(){}

    public void sendRoleAssignedMail(){}

    public void sendRoleRemovedMail(){}

    public void sendAccountBlockedMail(){}

    public void sendAccountUnBlockedMail(){}

    public void sendProfileUpdatedMail(){}

    public void sendRefreshTokenMail(){}

    public void sendDeviceLoginAlert(){}

    public void sendDeleteUserMail(){}

    public void sendLogoutMail(){}

}
