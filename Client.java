import java.io.*;
import java.net.Socket;

public class Client {
    private Socket socket;
    private String ip;
    private String name;
    private int port;
    private PrintWriter out;
    private BufferedReader in;

    public Client(String name, String ip, int port){
        this.name = name;
        this.ip = ip;
        this.port = port;
    }

    public void connect(){
        try {
            socket = new Socket(ip,port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }catch (IOException e){
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    public void sendMessage(BufferedReader stdIn) {
        try {
            System.out.println("Receiver:");
            String receiver = stdIn.readLine();
            System.out.println("Message:");
            String message = stdIn.readLine();
            out.println(receiver+" "+message);
            System.out.println(receiver+" "+message);
        }catch (IOException e){
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    public void sendTopic(BufferedReader stdIn) {
        try {
            System.out.println("Message type:");
            String type = stdIn.readLine();
            System.out.println("Time:");
            int time = Integer.parseInt(stdIn.readLine());
            System.out.println("Message:");
            String message = stdIn.readLine();
            out.println(type+" "+time+" "+message);
            System.out.println(type+" "+time+" "+message);
        }catch (IOException e){
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    public void readMessage() {
        try {
            String message;
            while((message = in.readLine())!=null) {
                System.out.println(message);
            }
        }catch (IOException e){
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    public void clean(BufferedReader stdIn){
	try{
	    stdIn.close();
	    in.close();
	    out.close();
	}catch(IOException e){
		System.out.println(e.getMessage());
                System.exit(-1);
	    }
     }
    

    public void readTopic(BufferedReader stdIn) {
        try {
            String message,type;
            System.out.println("Message type:");
            type = stdIn.readLine();
            out.println(type);
            while((message = in.readLine())!=null) {
                System.out.println(message);
            }
        }catch (IOException e){
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    public static void main(String[] arg){

        if(arg.length !=3 ){
            System.err.println("Usage: java Client <username> <host name> <port number> ");
            System.exit(1);
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        Client client = new Client(arg[0],arg[1],Integer.parseInt(arg[2]));
        client.connect();

        while(true){
            System.out.println("1 Send message");
            System.out.println("2 Send topic");
            System.out.println("3 Read message");
            System.out.println("4 Read topic");
            System.out.println("5 Leave");
            try {
                int op = Integer.parseInt(in.readLine());
                switch(op){
                    case 1: client.sendMessage(in);
                            break;
                    case 2: client.sendTopic(in);
                            break;
                    case 3: client.readMessage();
                            break;
                    case 4: client.readTopic(in);
                            break;
	            case 5: {client.clean(in);
			     return;
		            }
                    default:System.out.println("invalid operation");

                }
            }catch (IOException e){
                System.out.println(e.getMessage());
                System.exit(-1);
            }

        }
    }

}
