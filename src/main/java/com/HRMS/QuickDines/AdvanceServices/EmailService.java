package com.HRMS.QuickDines.AdvanceServices;

import com.HRMS.QuickDines.Company.model.Branch;
import com.HRMS.QuickDines.Company.model.Company;
import com.HRMS.QuickDines.Recruitment.model.Application;
import com.HRMS.QuickDines.Recruitment.model.Interview;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

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

    public void sendInvoiceEmail(
            String customerEmail,
            String invoiceNumber,
            byte[] pdf) {

        try {

            MimeMessage message =
                    mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(
                            message,
                            true);

            helper.setTo(customerEmail);

            helper.setSubject(
                    "QuickDine Invoice - "
                            + invoiceNumber);

            helper.setText(
                    """
                    Dear Customer,

                    Please find your QuickDine invoice
                    attached to this email.

                    Invoice Number: %s

                    Thank you for choosing QuickDine.

                    Regards,
                    QuickDine Team
                    """.formatted(invoiceNumber));

            helper.addAttachment(
                    "Invoice-"
                            + invoiceNumber
                            + ".pdf",
                    new ByteArrayResource(pdf));

            mailSender.send(message);

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to send invoice email",
                    e);
        }
    }
    public void sendInterviewEmail(
            Interview interview) {

        Application application =
                interview.getApplication();

        Branch branch =
               new Branch();

        Company company =
                branch.getCompany();

        String candidateEmail =
                application.getEmail();

        String candidateName =
                application.getCandidateName();

        String companyName =
                company.getCompanyName();

        String branchName =
                branch.getBranchName();

        String interviewType =
                interview.getInterviewType();

        String interviewer =
                interview.getInterviewerName();

        String interviewDate =
                interview.getInterviewDate()
                        .format(
                                DateTimeFormatter.ofPattern(
                                        "dd-MM-yyyy hh:mm a"));

        SimpleMailMessage message =
                new SimpleMailMessage();

        message.setTo(candidateEmail);

        message.setSubject(
                "Interview Scheduled - "
                        + companyName);

        message.setText(
                "Dear " + candidateName + ",\n\n"

                        + "Congratulations! You have been "
                        + "shortlisted for an interview.\n\n"

                        + "Company: "
                        + companyName + "\n"

                        + "Branch: "
                        + branchName + "\n"

                        + "Interview Type: "
                        + interviewType + "\n"

                        + "Interviewer: "
                        + interviewer + "\n"

                        + "Interview Date & Time: "
                        + interviewDate + "\n\n"

                        + "Please be available at the scheduled "
                        + "date and time.\n\n"

                        + "Regards,\n"
                        + companyName + "\n"
                        + "HR Team");

        mailSender.send(message);
    }

    public void sendSelectionEmail(
            Interview interview) {

        Application application =
                interview.getApplication();

        Branch branch =
                new Branch();

        Company company =
                branch.getCompany();

        String candidateName =
                application.getCandidateName();

        String candidateEmail =
                application.getEmail();

        String companyName =
                company.getCompanyName();

        String branchName =
                branch.getBranchName();

        SimpleMailMessage message =
                new SimpleMailMessage();

        message.setTo(candidateEmail);

        message.setSubject(
                "Congratulations! You Have Been Selected - "
                        + companyName);

        message.setText(
                "Dear " + candidateName + ",\n\n"

                        + "Congratulations!\n\n"

                        + "We are pleased to inform you that you "
                        + "have been selected for the position.\n\n"

                        + "Company: "
                        + companyName + "\n"

                        + "Branch: "
                        + branchName + "\n"

                        + "Interviewer: "
                        + interview.getInterviewerName()
                        + "\n\n"

                        + "Our HR team will contact you with the "
                        + "next steps regarding your joining process.\n\n"

                        + "Regards,\n"
                        + companyName + "\n"
                        + "HR Team");

        mailSender.send(message);
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
