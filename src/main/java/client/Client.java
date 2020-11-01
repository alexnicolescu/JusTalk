package src.main.java.client;

import java.io.*;
import java.net.Socket;

public class Client {
    private Socket socket;
    private final String ip;
    private final String name;
    private final int port;
    private PrintWriter out;
    private BufferedReader in;

    public Client(String name, String ip, int port) {
        this.name = name;
        this.ip = ip;
        this.port = port;
    }

    public void connect() {
        try {
            socket = new Socket(ip, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    public void sendName() {
        out.println(name);
    }

    public void sendMessage(BufferedReader stdIn) {
        try {
            System.out.println("Receiver:");
            String receiver = stdIn.readLine();
            System.out.println("Message:");
            String message = stdIn.readLine();
            out.println("1" + receiver + ": " + message);
            System.out.println(receiver + " " + message);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    public void sendTopic(BufferedReader stdIn) {
        try {
            System.out.println("Message type:");
            String type = stdIn.readLine();
            System.out.println("Time:");
            double time = Double.parseDouble(stdIn.readLine());
            System.out.println("Message:");
            String message = stdIn.readLine();
            out.println("2" + type + ":" + message + ":" + time);
            System.out.println(type + " " + time + " " + message);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    public void readMessage() {
        try {
            String message;
            out.println("3");
            while ((message = in.readLine()) == null) ;
            System.out.println(message);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    public void clean(BufferedReader stdIn) {
        try {
            out.println("logout");
            socket.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    public void readTopic(BufferedReader stdIn) {
        try {
            String message, type;
            System.out.println("Message type:");
            type = stdIn.readLine();
            out.println("4" + type);
            message = in.readLine();
            System.out.println(message);

        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    public static void main(String[] arg) {

        if (arg.length != 3) {
            System.err.println("Usage: java src.main.java.client.Client <username> <host name> <port number> ");
            System.exit(1);
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        Client client = new Client(arg[0], arg[1], Integer.parseInt(arg[2]));
        client.connect();
        client.sendName();

        while (true) {
            System.out.println("1 Send message");
            System.out.println("2 Send topic");
            System.out.println("3 Read message");
            System.out.println("4 Read topic");
            System.out.println("5 Leave");
            try {
                int op = Integer.parseInt(in.readLine());
                switch (op) {
                    case 1:
                        client.sendMessage(in);
                        break;
                    case 2:
                        client.sendTopic(in);
                        break;
                    case 3:
                        client.readMessage();
                        break;
                    case 4:
                        client.readTopic(in);
                        break;
                    case 5: {
                        client.clean(in);
                        return;
                    }
                    default:
                        System.out.println("invalid operation");

                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
                System.exit(-1);
            }

        }
    }

}
