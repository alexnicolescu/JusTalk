package src.main.java.server;

import src.main.java.server.topic.Administration;

import java.io.*;
import java.net.*;
import java.util.Timer;
import java.util.concurrent.ConcurrentSkipListMap;

public class Server {

    private static final int PORT_NUMBER = 8080;

    private final ConcurrentSkipListMap<String, Socket> clients = new ConcurrentSkipListMap<>();

    private ServerSocket serverSocket;
    private Timer timer;

    public Server() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new Administration(),0,5000);
    }

    public void acceptConnection() throws IOException {
        Socket clientSocket = serverSocket.accept();
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String clientName = in.readLine().toLowerCase();
        clients.put(clientName, clientSocket);
        ClientHandler clientHandler = new ClientHandler(out, in, clientName, this);
        clientHandler.start();
    }

    public void removeClient(String clientName) {
        clients.remove(clientName);
    }

    public static void main(String[] args) {
        Server server = new Server();
        try {
            server.serverSocket = new ServerSocket(PORT_NUMBER);
            while (true) {
                System.out.println("Waiting for Connection");
                server.acceptConnection();
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + PORT_NUMBER + " " + e.getMessage());
            System.exit(-1);
        } finally {
            try {
                server.serverSocket.close();
            } catch (IOException e) {
                System.err.println("Could not close port: " + PORT_NUMBER + " " + e.getMessage());
                System.exit(-1);
            }
        }
    }

}
