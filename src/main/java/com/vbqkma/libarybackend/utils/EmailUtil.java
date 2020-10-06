package com.vbqkma.libarybackend.utils;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Date;
import java.util.Properties;

public class EmailUtil {
    public static void sendMail(String host, String port, final String userName, final String password, String toAddress, String subject, String htmlBody)
            throws AddressException, MessagingException {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.user", userName);
        properties.put("mail.password", password);
        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        };
        Session session = Session.getInstance(properties, auth);
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(userName));
        InternetAddress[] toAddresses = {new InternetAddress(toAddress)};
        msg.setRecipients(Message.RecipientType.TO, toAddresses);
        msg.setSubject(subject);
        msg.setSentDate(new Date());
        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(htmlBody, "text/html; charset=" + "UTF-8");
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);
        msg.setContent(multipart);
        Transport.send(msg);
    }

    public static String getContent(String title, String user, String content ,String titleButton, String hrefButton) {
        StringBuffer body
                = new StringBuffer("<div style=\"border-style:solid;border-width:thin;border-color:#dadce0;border-radius:8px;padding:40px 20px\"\n" +
                "     align=\"center\" class=\"m_2188545635843415607mdv2rw\">\n" +
                "  <img\n" +
                "    src=\"http://actvn.edu.vn/images/logoico.png\"\n" +
                "    width=\"24\" height=\"24\" aria-hidden=\"true\" style=\"margin-bottom:16px\" alt=\"Google\" class=\"CToWUd\">\n" +
                "  <div\n" +
                "    style=\"font-family:'Google Sans',Roboto,RobotoDraft,Helvetica,Arial,sans-serif;border-bottom:thin solid #dadce0;color:rgba(0,0,0,0.87);line-height:32px;padding-bottom:24px;text-align:center;word-break:break-word\">\n" +
                "    <div style=\"font-size:24px\">"+title+"</div>\n" +
                "    <table align=\"center\" style=\"margin-top:8px\">\n" +
                "      <tbody>\n" +
                "      <tr style=\"line-height:normal\">\n" +
                "        <td align=\"right\" style=\"padding-right:8px\"><img width=\"20\" height=\"20\"\n" +
                "                                                         style=\"width:20px;height:20px;vertical-align:sub;border-radius:50%\"\n" +
                "                                                         src=\"https://lh3.googleusercontent.com/a-/AOh14GgPrxmiadh66X_Jn-_2bOQ-JXeGlgn4WBCHfzRXwQ=s96\"\n" +
                "                                                         alt=\"\" class=\"CToWUd\"></td>\n" +
                "        <td><a\n" +
                "          style=\"font-family:'Google Sans',Roboto,RobotoDraft,Helvetica,Arial,sans-serif;color:rgba(0,0,0,0.87);font-size:14px;line-height:20px\">"+user+"</a>\n" +
                "        </td>\n" +
                "      </tr>\n" +
                "      </tbody>\n" +
                "    </table>\n" +
                "  </div>\n" +
                "  <div\n" +
                "    style=\"font-family:Roboto-Regular,Helvetica,Arial,sans-serif;font-size:14px;color:rgba(0,0,0,0.87);line-height:20px;padding-top:20px;text-align:left\">\n" +
                "    "+content+"\n" +
                "    <div style=\"padding-top:32px;text-align:center\">\n" +
                "      <a\n" +
                "        href=\""+hrefButton+"\"\n" +
                "        style=\"font-family:'Google Sans',Roboto,RobotoDraft,Helvetica,Arial,sans-serif;line-height:16px;color:#ffffff;font-weight:400;text-decoration:none;font-size:14px;display:inline-block;padding:10px 24px;background-color:#4184f3;border-radius:5px;min-width:90px\"\n" +
                "        target=\"_blank\"\n" +
                "      >"+titleButton+"</a></div>\n" +
                "  </div>\n" +
                "</div>\n" +
                "<div style=\"text-align:left\">\n" +
                "  <div\n" +
                "    style=\"font-family:Roboto-Regular,Helvetica,Arial,sans-serif;color:rgba(0,0,0,0.54);font-size:11px;line-height:18px;padding-top:12px;text-align:center\">\n" +
                "    <div>Chúng tôi gửi email này để thông báo cho bạn biết về những thay đổi quan trọng đối với Tài khoản và\n" +
                "      dịch vụ của bạn.\n" +
                "    </div>\n" +
                "    <div style=\"direction:ltr\">© 2020 Vũ Bá Quang - Học viện Kỹ thuật mật mã</div>\n" +
                "  </div>\n" +
                "</div>\n");
        return body.toString();
    }
}
