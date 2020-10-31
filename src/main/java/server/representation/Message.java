package src.main.java.server.representation;

public class Message {

    public String message;

    public String receiver;

    public String sender;

    public Message(String message, String receiver, String sender) {
        this.message = message;
        this.receiver = receiver;
        this.sender = sender;
    }

    public String toString() {
        return sender + ":" + message;
    }

}
