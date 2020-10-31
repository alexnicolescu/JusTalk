package src.main.java.server.topic;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;

public class Topic {
    private final ConcurrentHashMap<String, ConcurrentSkipListSet<TopicMessage>> messages;
    private static Topic singleton;

    private Topic() {
        messages = new ConcurrentHashMap<>();
    }

    public void addMessage(TopicMessage message) {
        if (messages.get(message.getType()) == null) {
            messages.put(message.getType(), new ConcurrentSkipListSet<>());
        }
        messages.get(message.getType()).add(message);
    }

    public List<String> getMessages(String type) {
        return messages.get(type).stream().map(TopicMessage::getContent).collect(Collectors.toList());
    }

    public void removeOldMessages() {
        for (Map.Entry<String, ConcurrentSkipListSet<TopicMessage>> entry : messages.entrySet()) {
            ConcurrentSkipListSet<TopicMessage> topics = entry.getValue();
            for (TopicMessage topic : topics) {
                if (topic.isExpired()) {
                    System.out.println("Topic of type " + topic.getType()+ " with message " + topic.getContent() + " expired.");
                    topics.remove(topic);
                }
            }
        }
    }

    public static Topic newInstance() {
        if (singleton == null) {
            singleton = new Topic();
        }
        return singleton;
    }

}
