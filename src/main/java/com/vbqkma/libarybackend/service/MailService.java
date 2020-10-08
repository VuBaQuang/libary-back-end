package com.vbqkma.libarybackend.service;

import com.vbqkma.libarybackend.utils.EmailUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Service
public class MailService {
    private static final Logger logger = LogManager.getLogger(MailService.class);

    @Async("threadPoolTaskExecutor")
    public void sendMail(String receiver, String subject, String title, String user, String content, String titleButton, String hrefButton) {
        logger.info(Thread.currentThread().getName() + ": Execute method send mail " + receiver + " - " + subject);
        String host = "smtp.gmail.com";
        String port = "587";
        String sender = "vbq231198@gmail.com";
        String password = "0312673967";
        String bodyContent = EmailUtil.getContent(title, user, content, titleButton, hrefButton);
        try {
            EmailUtil.sendMail(host, port, sender, password, receiver,
                    subject, bodyContent);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
