package com.antoni.exchange;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

public class TestMessageChannel implements MessageChannel {

    @Override
    public boolean send(Message<?> message) {
        System.out.println(message);
        return MessageChannel.super.send(message);
    }

    @Override
    public boolean send(Message<?> message, long timeout) {
        System.out.println(message);
        return true;
    }
}
