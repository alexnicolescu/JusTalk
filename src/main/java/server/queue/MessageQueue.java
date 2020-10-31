package src.main.java.server.queue;

import src.main.java.server.representation.Message;

import java.util.concurrent.ConcurrentLinkedQueue;

public class MessageQueue {

    private final ConcurrentLinkedQueue<Message> queue;

    private static MessageQueue singleton;

    private MessageQueue() {
        queue = new ConcurrentLinkedQueue<>();
    }

    public Message retrieveMessage(String requester) {
        Message peek = queue.peek();
        if (peek != null && peek.receiver.equals(requester)) {
            return queue.poll();
        }
        return new Message("No message is available", requester, "System");
    }

    public void addMessage(Message message) {
        queue.add(message);
    }

    public static MessageQueue newInstance() {
        if (singleton == null) {
            singleton = new MessageQueue();
        }
        return singleton;
    }

}
