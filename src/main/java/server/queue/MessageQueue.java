package src.main.java.server.queue;

import src.main.java.server.representation.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MessageQueue {

    private final ConcurrentLinkedQueue<Message> queue;

    private static MessageQueue singleton;

    private static final List<String> users = new ArrayList<>();

    private MessageQueue() {
        queue = new ConcurrentLinkedQueue<>();
    }

    public Message retrieveMessage(String requester) {
        Message peek = queue.peek();
        while (peek != null && !users.contains(peek.receiver)) {
            queue.poll();
            peek = queue.peek();
        }
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

    public static void addUser(String userName) {
        users.add(userName);
    }

    public static void removeUser(String userName) {
        users.remove(userName);
    }

}
