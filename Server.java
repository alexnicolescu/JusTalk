import java.io.*;
import java.net.*;
import java.util.concurrent.ConcurrentSkipListMap;

public class Server {
    private static final int PORT_NUMBER = 8080;
    private ConcurrentSkipListMap<String, Socket> clients = new ConcurrentSkipListMap<String, Socket>();
    private ServerSocket serverSocket;

    public void acceptConnection() throws IOException {
        Socket clientSocket = serverSocket.accept();
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String clientName = in.readLine().toLowerCase();
        clients.put(clientName, clientSocket);
        ClientHandler clientHandler = new ClientHandler(clientSocket, out, in, clientName, this);
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

class ClientHandler extends Thread {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private String username;
    private Server server;

    public ClientHandler(Socket clientSocket, PrintWriter out, BufferedReader in, String username, Server server) {
        this.clientSocket = clientSocket;
        this.out = out;
        this.in = in;
        this.username = username;
        this.server = server;
    }

    public void selectOption(String message) {
        String option = message.charAt(0) + "";
        message = message.substring(1);
        switch (option) {
            case "1": 
            System.out.println(username + " sent a message");
                break;
            case "2": 
            System.out.println(username + " sent a topic");
                break;
            case "3": 
            System.out.println(username + " wants to read a message");
                break;
            case "4": 
            System.out.println(username + " wants to read a topic");
                break;
            default:
                System.out.println("invalid operation");
        }
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
                System.err.println("Problem with Communication Server" + e.getMessage());
                System.exit(-1);
            }
        }

    }

}