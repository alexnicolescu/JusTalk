package src.main.java.server.topic;

import java.util.TimerTask;

public class Administration extends TimerTask {

    @Override
    public void run() {
        Topic.newInstance().removeOldMessages();
    }
}
