import java.io.*;
import java.net.*;
class Server extends Thread {
    private Socket clientSocket;

    public static void main(String[] args) {
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(8080);
                while (true) {
                    System.out.println("Waiting for Connection");
                    Server server = new Server(serverSocket.accept());
                    server.start();
                }
        } catch (IOException e) {
            System.err.println("Could not listen on port: 8080" + e.getMessage());
            System.exit(-1);
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                System.err.println("Could not close port: 8080"+ e.getMessage());
                System.exit(-1);
            }
        }
    }

    public Server(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),
                    true);
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    clientSocket.getInputStream()));
            out.println("message");

        } catch (IOException e) {
            System.err.println("Problem with Communication Server" + e.getMessage());
            System.exit(1);
        }
    }
}