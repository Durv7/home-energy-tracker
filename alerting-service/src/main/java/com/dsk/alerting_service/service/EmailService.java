package com.dsk.alerting_service.service;

import com.dsk.kafka.event.AlertKafkaEvent;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    private final String fromAddress;

    @Getter
    private String alertTemplate= "<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "\n" +
            "    <meta name=\"viewport\"\n" +
            "          content=\"width=device-width, initial-scale=1.0\">\n" +
            "\n" +
            "    <title>Energy Usage Alert</title>\n" +
            "</head>\n" +
            "\n" +
            "<body style=\"\n" +
            "    margin: 0;\n" +
            "    padding: 0;\n" +
            "    background-color: #f4f6f8;\n" +
            "    font-family: Arial, Helvetica, sans-serif;\n" +
            "\">\n" +
            "\n" +
            "    <div style=\"\n" +
            "        max-width: 600px;\n" +
            "        margin: 30px auto;\n" +
            "        background-color: #ffffff;\n" +
            "        border-radius: 12px;\n" +
            "        overflow: hidden;\n" +
            "        box-shadow: 0 4px 15px rgba(0,0,0,0.08);\n" +
            "    \">\n" +
            "\n" +
            "        <!-- Header -->\n" +
            "        <div style=\"\n" +
            "            background-color: #1e293b;\n" +
            "            padding: 25px;\n" +
            "            text-align: center;\n" +
            "        \">\n" +
            "\n" +
            "            <h1 style=\"\n" +
            "                margin: 0;\n" +
            "                color: #ffffff;\n" +
            "                font-size: 24px;\n" +
            "            \">\n" +
            "                ⚡ Home Energy Tracker\n" +
            "            </h1>\n" +
            "\n" +
            "            <p style=\"\n" +
            "                margin: 8px 0 0;\n" +
            "                color: #cbd5e1;\n" +
            "                font-size: 14px;\n" +
            "            \">\n" +
            "                Energy Consumption Alert\n" +
            "            </p>\n" +
            "\n" +
            "        </div>\n" +
            "\n" +
            "\n" +
            "        <!-- Main Content -->\n" +
            "        <div style=\"padding: 30px;\">\n" +
            "\n" +
            "            <h2 style=\"\n" +
            "                color: #dc2626;\n" +
            "                font-size: 22px;\n" +
            "                margin-top: 0;\n" +
            "            \">\n" +
            "                ⚠\uFE0F Energy Usage Limit Exceeded\n" +
            "            </h2>\n" +
            "\n" +
            "            <p style=\"\n" +
            "                color: #334155;\n" +
            "                font-size: 16px;\n" +
            "                line-height: 1.6;\n" +
            "            \">\n" +
            "                Hello <strong>{{fullName}}</strong>,\n" +
            "            </p>\n" +
            "\n" +
            "            <p style=\"\n" +
            "                color: #475569;\n" +
            "                font-size: 15px;\n" +
            "                line-height: 1.6;\n" +
            "            \">\n" +
            "                Your home energy consumption has exceeded\n" +
            "                the alert threshold you configured.\n" +
            "                Please review your energy usage.\n" +
            "            </p>\n" +
            "\n" +
            "\n" +
            "            <!-- Alert Status -->\n" +
            "            <div style=\"\n" +
            "                background-color: #fef2f2;\n" +
            "                border-left: 5px solid #dc2626;\n" +
            "                padding: 15px;\n" +
            "                margin: 25px 0;\n" +
            "                border-radius: 6px;\n" +
            "            \">\n" +
            "\n" +
            "                <p style=\"\n" +
            "                    margin: 0;\n" +
            "                    color: #991b1b;\n" +
            "                    font-size: 15px;\n" +
            "                \">\n" +
            "\n" +
            "                    <strong>Alert Status:</strong>\n" +
            "                    {{alerting}}\n" +
            "\n" +
            "                </p>\n" +
            "\n" +
            "            </div>\n" +
            "\n" +
            "\n" +
            "            <!-- Energy Details -->\n" +
            "            <h3 style=\"\n" +
            "                color: #1e293b;\n" +
            "                font-size: 18px;\n" +
            "                margin-bottom: 15px;\n" +
            "            \">\n" +
            "                Energy Usage Details\n" +
            "            </h3>\n" +
            "\n" +
            "\n" +
            "            <table style=\"\n" +
            "                width: 100%;\n" +
            "                border-collapse: collapse;\n" +
            "                font-size: 15px;\n" +
            "            \">\n" +
            "\n" +
            "                <tr>\n" +
            "                    <td style=\"\n" +
            "                        padding: 12px 0;\n" +
            "                        color: #64748b;\n" +
            "                        border-bottom: 1px solid #e2e8f0;\n" +
            "                    \">\n" +
            "                        User Name\n" +
            "                    </td>\n" +
            "\n" +
            "                    <td style=\"\n" +
            "                        padding: 12px 0;\n" +
            "                        color: #1e293b;\n" +
            "                        text-align: right;\n" +
            "                        border-bottom: 1px solid #e2e8f0;\n" +
            "                    \">\n" +
            "                        <strong>{{fullName}}</strong>\n" +
            "                    </td>\n" +
            "                </tr>\n" +
            "\n" +
            "\n" +
            "                <tr>\n" +
            "                    <td style=\"\n" +
            "                        padding: 12px 0;\n" +
            "                        color: #64748b;\n" +
            "                        border-bottom: 1px solid #e2e8f0;\n" +
            "                    \">\n" +
            "                        Email\n" +
            "                    </td>\n" +
            "\n" +
            "                    <td style=\"\n" +
            "                        padding: 12px 0;\n" +
            "                        color: #1e293b;\n" +
            "                        text-align: right;\n" +
            "                        border-bottom: 1px solid #e2e8f0;\n" +
            "                    \">\n" +
            "                        {{email}}\n" +
            "                    </td>\n" +
            "                </tr>\n" +
            "\n" +
            "\n" +
            "                <tr>\n" +
            "                    <td style=\"\n" +
            "                        padding: 12px 0;\n" +
            "                        color: #64748b;\n" +
            "                        border-bottom: 1px solid #e2e8f0;\n" +
            "                    \">\n" +
            "                        Alert Enabled\n" +
            "                    </td>\n" +
            "\n" +
            "                    <td style=\"\n" +
            "                        padding: 12px 0;\n" +
            "                        color: #1e293b;\n" +
            "                        text-align: right;\n" +
            "                        border-bottom: 1px solid #e2e8f0;\n" +
            "                    \">\n" +
            "                        {{alerting}}\n" +
            "                    </td>\n" +
            "                </tr>\n" +
            "\n" +
            "\n" +
            "                <tr>\n" +
            "                    <td style=\"\n" +
            "                        padding: 12px 0;\n" +
            "                        color: #64748b;\n" +
            "                        border-bottom: 1px solid #e2e8f0;\n" +
            "                    \">\n" +
            "                        Set Energy Threshold\n" +
            "                    </td>\n" +
            "\n" +
            "                    <td style=\"\n" +
            "                        padding: 12px 0;\n" +
            "                        color: #1e293b;\n" +
            "                        text-align: right;\n" +
            "                        border-bottom: 1px solid #e2e8f0;\n" +
            "                    \">\n" +
            "                        <strong>\n" +
            "                            {{energyAlertingThreshold}} kWh\n" +
            "                        </strong>\n" +
            "                    </td>\n" +
            "                </tr>\n" +
            "\n" +
            "\n" +
            "                <tr>\n" +
            "                    <td style=\"\n" +
            "                        padding: 12px 0;\n" +
            "                        color: #64748b;\n" +
            "                    \">\n" +
            "                        Current Energy Consumed\n" +
            "                    </td>\n" +
            "\n" +
            "                    <td style=\"\n" +
            "                        padding: 12px 0;\n" +
            "                        color: #dc2626;\n" +
            "                        text-align: right;\n" +
            "                        font-size: 16px;\n" +
            "                    \">\n" +
            "                        <strong>\n" +
            "                            {{energyConsumed}} kWh\n" +
            "                        </strong>\n" +
            "                    </td>\n" +
            "                </tr>\n" +
            "\n" +
            "            </table>\n" +
            "\n" +
            "\n" +
            "            <!-- Recommendation -->\n" +
            "            <div style=\"\n" +
            "                margin-top: 25px;\n" +
            "                padding: 18px;\n" +
            "                background-color: #f8fafc;\n" +
            "                border-radius: 8px;\n" +
            "            \">\n" +
            "\n" +
            "                <h3 style=\"\n" +
            "                    margin-top: 0;\n" +
            "                    color: #1e293b;\n" +
            "                    font-size: 16px;\n" +
            "                \">\n" +
            "                    \uD83D\uDCA1 What You Can Do\n" +
            "                </h3>\n" +
            "\n" +
            "                <p style=\"\n" +
            "                    margin-bottom: 0;\n" +
            "                    color: #475569;\n" +
            "                    font-size: 14px;\n" +
            "                    line-height: 1.6;\n" +
            "                \">\n" +
            "                    Consider checking your high-consumption\n" +
            "                    devices and reducing unnecessary energy usage\n" +
            "                    to stay within your preferred limit.\n" +
            "                </p>\n" +
            "\n" +
            "            </div>\n" +
            "\n" +
            "\n" +
            "            <p style=\"\n" +
            "                margin-top: 30px;\n" +
            "                color: #475569;\n" +
            "                font-size: 14px;\n" +
            "                line-height: 1.6;\n" +
            "            \">\n" +
            "                Thank you for using\n" +
            "                <strong>Home Energy Tracker</strong>.\n" +
            "                We are here to help you monitor your energy\n" +
            "                consumption.\n" +
            "            </p>\n" +
            "\n" +
            "        </div>\n" +
            "\n" +
            "\n" +
            "        <!-- Footer -->\n" +
            "        <div style=\"\n" +
            "            background-color: #f8fafc;\n" +
            "            padding: 20px;\n" +
            "            text-align: center;\n" +
            "            border-top: 1px solid #e2e8f0;\n" +
            "        \">\n" +
            "\n" +
            "            <p style=\"\n" +
            "                margin: 0;\n" +
            "                color: #94a3b8;\n" +
            "                font-size: 12px;\n" +
            "            \">\n" +
            "                This is an automated email.\n" +
            "                Please do not reply to this message.\n" +
            "            </p>\n" +
            "\n" +
            "            <p style=\"\n" +
            "                margin: 8px 0 0;\n" +
            "                color: #94a3b8;\n" +
            "                font-size: 12px;\n" +
            "            \">\n" +
            "                © 2026 Home Energy Tracker\n" +
            "            </p>\n" +
            "\n" +
            "        </div>\n" +
            "\n" +
            "    </div>\n" +
            "\n" +
            "</body>\n" +
            "\n" +
            "</html>\n";

    public EmailService(JavaMailSender mailSender, @Value("${alert.service.from_address}") String fromAddress) {
        this.mailSender = mailSender;
        this.fromAddress = fromAddress;
    }


    public void sendMimeMessage(String to , String subject,  String body ) throws MessagingException {

        log.info("Sending email to {}" ,to);

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);

        messageHelper.setTo(to);
        messageHelper.setFrom(fromAddress);
        messageHelper.setSubject(subject);
        messageHelper.setText(body ,true);

        mailSender.send(mimeMessage);
    }
}
