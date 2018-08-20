package controller;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

import javafx.animation.AnimationTimer;
import javafx.scene.control.TextArea;

public class MessageConsumer extends AnimationTimer {
    private final BlockingQueue<String> messageQueue;
    private final TextArea textArea_Logs;

    public MessageConsumer(BlockingQueue<String> messageQueue, TextArea textArea_Logs) {
        this.messageQueue = messageQueue;
        this.textArea_Logs = textArea_Logs;
    }

    @Override
    public void handle(long now) {
        var messages = new ArrayList<String>();
        messageQueue.drainTo(messages);
        messages.forEach(msg -> textArea_Logs.appendText(msg + "\n"));
    }
}