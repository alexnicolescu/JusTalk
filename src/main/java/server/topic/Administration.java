package src.main.java.server.topic;

import java.util.TimerTask;

public class Administration extends TimerTask {
    private double maxTopicTime;

    public Administration(double maxTopicTime) {
        this.maxTopicTime=maxTopicTime;
    }

    @Override
    public void run() {
        Topic.newInstance().removeOldMessages(maxTopicTime);
    }
}
