package controller;

import javafx.animation.AnimationTimer;
import javafx.scene.control.TextArea;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class UpdateLog extends AnimationTimer {
    private final LinkedBlockingQueue<String> messageQueue;
    private final TextArea textArea_Logs;

    public UpdateLog(LinkedBlockingQueue<String> messageQueue, TextArea textArea_Logs) {
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