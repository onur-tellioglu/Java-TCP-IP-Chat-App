import java.io.*;
import java.net.Socket;

public class Client implements Runnable {
    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private boolean done;

    @Override //Creating a supertype function via Overriding Runnable function.
    public void run() {
        String hostIPv4 = "172.20.10.7";
        try{
            client = new Socket(hostIPv4, 9999); //Creating a client object using Socket's parameters. Host's IPV4 address and Server's assigned port should be entered as parameter.
            out = new PrintWriter(client.getOutputStream(),true);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            InputHandler inHandler = new InputHandler();
            Thread t = new Thread(inHandler);
            t.start(); //The "t" object will start JVM to execute.

            String inMessage; //inMessage variable is the input that user enters.
            while ((inMessage = in.readLine()) != null){
                System.out.println(inMessage);
            }
        } catch (IOException e){
            shutdown();
        }
    }
    public void shutdown(){ //This function shuts the system down by closing objects that program needed to use to be executed.
        done = true;
        try{
            in.close();
            out.close();
            if(!client.isClosed()){
                client.close();
            }
        }catch (IOException e){
            // ignore
        }
    }

    class InputHandler implements Runnable{
        @Override //Creating a supertype function via Overriding Runnable function.
        public void run() {
            try{
                BufferedReader inReader = new BufferedReader(new InputStreamReader(System.in));
                while (!done){ //While system up, program follows the code below.
                    String message = inReader.readLine();
                    if (message.equals("/quit") || message.equals("/exit") || message.equals("/left")){ //If user enters one of these texts as message, program shuts down.
                        out.println(message);
                        inReader.close();
                        shutdown();
                    }else{ //If enters another statement, then message will be displayed at all clients' screens.
                        out.println(message);
                    }
                }
            }catch (IOException e){
                shutdown();
            }
        }
    }

    public static void main(String[] args) {
        Client client = new Client(); //Creating a Client object from Client class.
        client.run(); //Then running it via run function.
    }
}