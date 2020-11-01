package src.main.java.server.topic;

import java.time.Instant;

public class TopicMessage implements Comparable {

    private String type;
    private String content;
    private Instant timePosted;
    private double duration;

    public TopicMessage(String message) {
        String[] content = message.split(":");
        this.type = content[0];
        this.content = content[1];
        this.duration = Double.parseDouble(content[2]);
        this.timePosted = Instant.now();
    }

    public String getType() {
        return type;
    }

    public String getContent() {
        return content;
    }


    public boolean isExpired(double maxTopicTime) {
        boolean expiredFromSelf =  this.timePosted.plusSeconds((long) (60*this.duration)).compareTo(Instant.now())<0;
        boolean expiredFromServer = this.timePosted.plusSeconds((long) (60*maxTopicTime)).compareTo(Instant.now())<0;
        return expiredFromSelf || expiredFromServer;
    }


    @Override
    public int compareTo(Object o) {
        return timePosted.compareTo(((TopicMessage) o).timePosted);
    }
}
