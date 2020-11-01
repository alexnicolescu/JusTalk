package src.main.java.server;

import src.main.java.server.queue.MessageQueue;
import src.main.java.server.representation.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class ClientHandler extends Thread {

    private final PrintWriter out;

    private final BufferedReader in;

    private final String username;

    private final Server server;

    private static final MessageQueue messageQueue = MessageQueue.newInstance();

    public ClientHandler(PrintWriter out, BufferedReader in, String username, Server server) {
        this.out = out;
        this.in = in;
        this.username = username;
        this.server = server;
        MessageQueue.addUser(username);
    }

    public void selectOption(String message) {
        String option = message.charAt(0) + "";
        message = message.substring(1);
        switch (option) {
            case "1":
                sendMessage(message);
                break;
            case "2":
                System.out.println(username + " sent a topic");
                break;
            case "3":
                retrieveMessage();
                break;
            case "4":
                System.out.println(username + " wants to read a topic");
                break;
            default:
                System.out.println("invalid operation");
        }
    }

    private void sendMessage(String message) {
        String[] messageInfo = message.split(":");
        String receiver = messageInfo[0];
        String content = "";
        if (messageInfo.length > 1) {
            content = messageInfo[1];
        }
        System.out.println(username + " sent a message");
        messageQueue.addMessage(new Message(content, receiver, username));
    }

    private void retrieveMessage() {
        System.out.println(username + " wants to read a message");
        Message retrievedMessage = messageQueue.retrieveMessage(username);
        out.println(retrievedMessage);
    }

    @Override
    public void run() {
        while (true) {
            try {
                String message = in.readLine();
                if (message.equals("logout")) {
                    System.out.println(username + " left!");
                    server.removeClient(username);
                    break;
                }
                selectOption(message);
            } catch (IOException e) {
                System.err.println("Problem with Communication src.main.java.server.Server " + e.getMessage());
                System.exit(-1);
            }
        }

    }

}
