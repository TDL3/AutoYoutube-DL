package controller;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

import javafx.animation.AnimationTimer;
import javafx.scene.control.TextArea;

public class MessageConsumer extends AnimationTimer {
	private final BlockingQueue<String> messageQueue;
	private final TextArea textArea_Logs;
	//private final int numMessages;
	//private int messagesReceived = 0;

//	public MessageConsumer(BlockingQueue<String> messageQueue, TextArea textArea_Logs, int numMessages) {
//		this.messageQueue = messageQueue;
//		this.textArea_Logs = textArea_Logs;
//		//this.numMessages = numMessages;
//	}
	
	public MessageConsumer(BlockingQueue<String> messageQueue, TextArea textArea_Logs) {
		this.messageQueue = messageQueue;
		this.textArea_Logs = textArea_Logs;
		//this.numMessages = 1;
	}

	@Override
	public void handle(long now) {
		var messages = new ArrayList<String>();
		messageQueue.drainTo(messages);
		messages.forEach(msg -> textArea_Logs.appendText(msg + "\n"));
//		if (messagesReceived >= numMessages) {
//			stop();
//		}
	}
}