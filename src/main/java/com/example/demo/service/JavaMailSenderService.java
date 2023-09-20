package com.example.demo.service;

import com.example.demo.entity.ActivateCodes;
import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@Slf4j
@RequiredArgsConstructor
public class JavaMailSenderService {
    public final JavaMailSender javaMailSender;

    @Async
    public void send(@NonNull ActivateCodes activateCodes,@NonNull String text) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            mimeMessage.setText(text+activateCodes.getCode());
            InternetAddress address = new InternetAddress();
            address.setAddress(activateCodes.getAuthUser().getEmail());
            mimeMessage.addRecipient(Message.RecipientType.TO,address);
            javaMailSender.send(mimeMessage);
            log.info("send message to {}",activateCodes.getAuthUser().getEmail());
        }catch (Exception e){
            e.printStackTrace();
            log.info("{}", Arrays.toString(e.getStackTrace()));
            throw new RuntimeException();
        }
    }
    @Async
    public void send(@NonNull String email,@NonNull String text){
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            mimeMessage.setText(text);
            InternetAddress address = new InternetAddress();
            address.setAddress(email);
            mimeMessage.addRecipient(Message.RecipientType.TO,address);
            javaMailSender.send(mimeMessage);
            log.info("message send to {}",email);
        }catch (Exception e){
            e.printStackTrace();
            log.info("{}", Arrays.toString(e.getStackTrace()));
            throw new RuntimeException();
        }
    }
}
