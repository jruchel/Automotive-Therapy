package org.jruchel.carworkshop.rabbitmq;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class MessageReceiver {
    public void receiveMessage(byte[] message) {
        System.out.println("Message received: " + new String(message, StandardCharsets.UTF_8));
    }
}
