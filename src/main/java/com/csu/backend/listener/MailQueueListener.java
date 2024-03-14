package com.csu.backend.listener;

import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Map;

// 邮件队列监听器
@Component
@RabbitListener(queues = "mail")
public class MailQueueListener {

    @Resource
    JavaMailSender sender;

    @Value("${spring.mail.username}")
    String username;

    /**
     * 使用RabbitMQ消息队列处理邮件发送的请求。
     * 它从传入的数据中获取邮箱地址、验证码和消息类型。
     * 根据消息类型，它创建一个邮件消息，然后发送这个消息。
     * 如果消息类型不是"register"或"reset"，则不会发送任何消息。
     *
     * @param data 包含邮件信息的Map对象，包括邮箱地址、验证码和消息类型
     */
    @RabbitHandler
    public void sendMailMessage(Map<String, Object> data) {
        // 获取邮箱
        String email = (String) data.get("email");
        // 获取验证码
        Integer code = (Integer) data.get("code");
        // 获取消息类型
        String type = (String) data.get("type");
        SimpleMailMessage message = switch (type) {
            case "register" -> createMessage("欢迎注册我们的网站",
                    "您的邮件注册码为" + code + "，有效时间3分钟，为了保障您的安全，请勿向他人泄露验证码信息。",
                    email);
            case "reset" -> createMessage("您的密码重置邮件",
                    "您好，您正在进行重置密码操作，验证码" + code + "，有效时间3分钟，如非本人操作，请无视。",
                    email);
            default -> null;
        };
        if (message == null) return;
        sender.send(message);
    }

    /**
     * 创建一个SimpleMailMessage对象，用于发送邮件。
     * 它设置邮件的主题、内容、收件人和发件人。
     *
     * @param title 邮件的主题
     * @param content 邮件的内容
     * @param email 收件人的邮箱地址
     * @return 创建的SimpleMailMessage对象
     */
    private SimpleMailMessage createMessage(String title, String content, String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(title);
        message.setText(content);
        message.setTo(email);
        message.setFrom(username);
        return message;
    }
}
