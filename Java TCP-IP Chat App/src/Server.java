import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.*;

public class Server implements Runnable {
    private ArrayList<ConnectionHandler> connections;
    private ServerSocket server;
    private boolean done;
    private ExecutorService pool;

    public Server(){
        connections = new ArrayList<>();
        done = false;
    }

    @Override
    public void run() {
        try{
            server = new ServerSocket(9999); //Assigning the port server runs on the network.
            pool = Executors.newCachedThreadPool();

            while (!done) {
                Socket client = server.accept();
                ConnectionHandler handler = new ConnectionHandler(client);
                connections.add(handler);
                pool.execute(handler);
            }
        } catch (Exception e) {
            shutdown();
        }
    }
    public void broadcast(String message){ //broadcast is the function that displaying the message to the clients.
        for(ConnectionHandler ch : connections){
            if (ch != null){
                ch.sendMessage(message);
            }
        }
    }

    public void shutdown(){
        try {
            done = true;
            pool.shutdown();

            if (!server.isClosed()) {
                server.close();
            }

            for (ConnectionHandler ch : connections){
                ch.shutdown();
            }
        } catch (IOException e){
            // ignore
        }
    }

    class ConnectionHandler implements Runnable {
        private Socket client;
        private BufferedReader in;
        private PrintWriter out;
        private String nickname;

        public ConnectionHandler(Socket client){
            this.client=client;
        }

        @Override
        public void run() {
            try {
                out = new PrintWriter(client.getOutputStream(),true);
                in = new BufferedReader(new InputStreamReader(client.getInputStream()));

                out.println("Please enter a nickname: ");
                nickname = in.readLine();
                System.out.println(nickname + " connected!\n");
                broadcast("\n"+ nickname + " joined the chat!\nTo see commands, type '/help'.\n");
                String message;
                while((message = in.readLine()) != null) {
                    if(message.startsWith("/nick ")){
                        String[] messageSplit = message.split(" ",2);
                        if (messageSplit.length == 2){
                            broadcast(nickname + " renamed themselves to "+ messageSplit[1] + "\n");
                            System.out.println(nickname + " renamed themselves to "+ messageSplit[1] + "\n");
                            nickname = messageSplit[1];
                            out.println("Successfully changed nickname to "+ nickname + "\n");
                        } else {
                            out.println("No nickname provided!\n");
                        }
                    }else if (message.equals("/quit") || message.equals("/exit") || message.equals("/left") || message.equals("/leave")){
                        broadcast(nickname+ " left the chat!\n");
                        shutdown();
                    } else if (message.equals("/help")) {
                        out.println("\nWelcome to the help screen.\nTo list people: /people\nTo change nickname: '/nick nick'\nTo quit the chat type '/quit', '/left', '/exit'\nEmoji List: '/emojis'");
                    } else if (message.equals("/people")) {
                        out.println("\nThere are " + connections.size() + " people online.");
                    } else if (message.equals("/emojis")) {
                        out.println("\n'':book:''\n'':booker:''\n'':frog:''\n'':harambe:''\n'':monke:''\n'':rabbit:''");
                    } else if (message.equals(":book:")) {
                        broadcast(nickname + ": \n" +
                                "       .--.                   .---.\n" +
                                "   .---|__|           .-.     |~~~|\n" +
                                ".--|===|--|_          |_|     |~~~|--.\n" +
                                "|  |===|  |'\\     .---!~|  .--|   |--|\n" +
                                "|%%|   |  |.'\\    |===| |--|%%|   |  |\n" +
                                "|%%|   |  |\\.'\\   |   | |__|  |   |  |\n" +
                                "|  |   |  | \\  \\  |===| |==|  |   |  |\n" +
                                "|  |   |__|  \\.'\\ |   |_|__|  |~~~|__|\n" +
                                "|  |===|--|   \\.'\\|===|~|--|%%|~~~|--|\n" +
                                "^--^---'--^    `-'`---^-^--^--^---'--'");
                    } else if (message.equals(":booker:")) {
                        broadcast(nickname + ": \n" +
                                "   ____________________________________________________\n" +
                                "  |____________________________________________________|\n" +
                                "  | __     __   ____   ___ ||  ____    ____     _  __  |\n" +
                                "  ||  |__ |--|_| || |_|   |||_|**|*|__|+|+||___| ||  | |\n" +
                                "  ||==|^^||--| |=||=| |=*=||| |~~|~|  |=|=|| | |~||==| |\n" +
                                "  ||  |##||  | | || | |JRO|||-|  | |==|+|+||-|-|~||__| |\n" +
                                "  ||__|__||__|_|_||_|_|___|||_|__|_|__|_|_||_|_|_||__|_|\n" +
                                "  ||_______________________||__________________________|\n" +
                                "  | _____________________  ||      __   __  _  __    _ |\n" +
                                "  ||=|=|=|=|=|=|=|=|=|=|=| __..\\/ |  |_|  ||#||==|  / /|\n" +
                                "  || | | | | | | | | | | |/\\ \\  \\\\|++|=|  || ||==| / / |\n" +
                                "  ||_|_|_|_|_|_|_|_|_|_|_/_/\\_.___\\__|_|__||_||__|/_/__|\n" +
                                "  |____________________ /\\~()/()~//\\ __________________|\n" +
                                "  | __   __    _  _     \\_  (_ .  _/ _    ___     _____|\n" +
                                "  ||~~|_|..|__| || |_ _   \\ //\\\\ /  |=|__|~|~|___| | | |\n" +
                                "  ||--|+|^^|==|1||2| | |__/\\ __ /\\__| |==|x|x|+|+|=|=|=|\n" +
                                "  ||__|_|__|__|_||_|_| /  \\ \\  / /  \\_|__|_|_|_|_|_|_|_|\n" +
                                "  |_________________ _/    \\/\\/\\/    \\_ _______________|\n" +
                                "  | _____   _   __  |/      \\../      \\|  __   __   ___|\n" +
                                "  ||_____|_| |_|##|_||   |   \\/ __|   ||_|==|_|++|_|-|||\n" +
                                "  ||______||=|#|--| |\\   \\   o    /   /| |  |~|  | | |||\n" +
                                "  ||______||_|_|__|_|_\\   \\  o   /   /_|_|__|_|__|_|_|||\n" +
                                "  |_________ __________\\___\\____/___/___________ ______|\n" +
                                "  |__    _  /    ________     ______           /| _ _ _|\n" +
                                "  |\\ \\  |=|/   //    /| //   /  /  / |        / ||%|%|%|\n" +
                                "  | \\/\\ |*/  .//____//.//   /__/__/ (_)      /  ||=|=|=|\n" +
                                "__|  \\/\\|/   /(____|/ //                    /  /||~|~|~|__\n" +
                                "  |___\\_/   /________//   ________         /  / ||_|_|_|\n" +
                                "  |___ /   (|________/   |\\_______\\       /  /| |______|\n" +
                                "      /                  \\|________)     /  / | |");
                    }else if (message.equals(":frog:")) {
                        broadcast(nickname + ": \n" +
                                "           .--._.--.\n" +
                                "          ( O     O )\n" +
                                "          /   . .   \\\n" +
                                "         .`._______.'.\n" +
                                "        /(           )\\\n" +
                                "      _/  \\  \\   /  /  \\_\n" +
                                "   .~   `  \\  \\ /  /  '   ~.\n" +
                                "  {    -.   \\  V  /   .-    }\n" +
                                "_ _`.    \\  |  |  |  /    .'_ _\n" +
                                ">_       _} |  |  | {_       _<\n" +
                                " /. - ~ ,_-'  .^.  `-_, ~ - .\\\n" +
                                "         '-'|/   \\|`-`");
                    } else if(message.equals(":harambe:")) {
                        broadcast(nickname + ": \n" +
                                "           .\"`\".\n" +
                                "       .-./ _=_ \\.-.\n" +
                                "      {  (,(oYo),) }}\n" +
                                "      {{ |   \"   |} }\n" +
                                "      { { \\(---)/  }}\n" +
                                "      {{  }'-=-'{ } }\n" +
                                "      { { }._:_.{  }}\n" +
                                "      {{  } -:- { } }\n" +
                                "      {_{ }`===`{  _}\n" +
                                "     ((((\\)     (/))))");
                    } else if (message.equals(":monke:")) {
                        broadcast(nickname + ": \n" +
                                "    || __   ||\n" +
                                "    ||=\\_`\\=||\n" +
                                "    || (__/ ||\n" +
                                "    ||  | | :-\"\"\"-.\n" +
                                "    ||==| \\/-=-.   \\\n" +
                                "    ||  |(_|o o/   |_\n" +
                                "    ||   \\/ \"  \\   ,_)\n" +
                                "    ||====\\ ^  /__/\n" +
                                "    ||     ;--'  `-.\n" +
                                "    ||    /      .  \\\n" +
                                "    ||===;        \\  \\\n" +
                                "    ||   |         | |\n" +
                                "    || .-\\ '     _/_/\n" +
                                "    |:'  _;.    (_  \\\n" +
                                "    /  .'  `;\\   \\\\_/\n" +
                                "   |_ /     |||  |\\\\\n" +
                                "  /  _)=====|||  | ||\n" +
                                " /  /|      ||/  / //\n" +
                                " \\_/||      ( `-/ ||\n" +
                                "    ||======/  /  \\\\ .-.\n" +
                                "    ||      \\_/    \\'-'/\n" +
                                "    ||      ||      `\"`\n" +
                                "    ||======||\n" +
                                "    ||      ||");
                    } else if (message.equals(":rabbit:")) {
                        broadcast(nickname + ": \n" +
                                "                              __\n" +
                                "                     /\\    .-\" /\n" +
                                "                    /  ; .'  .' \n" +
                                "                   :   :/  .'   \n" +
                                "                    \\  ;-.'     \n" +
                                "       .--\"\"\"\"--..__/     `.    \n" +
                                "     .'           .'    `o  \\   \n" +
                                "    /                    `   ;  \n" +
                                "   :                  \\      :  \n" +
                                " .-;        -.         `.__.-'  \n" +
                                ":  ;          \\     ,   ;       \n" +
                                "'._:           ;   :   (        \n" +
                                "    \\/  .__    ;    \\   `-.     \n" +
                                "     ;     \"-,/_..--\"`-..__)    \n" +
                                "     '\"\"--.._:");
                    } else {
                        broadcast(nickname + ": " + message);
                    }
                }
            } catch (IOException e){
                 shutdown();
            }
        }

        public void sendMessage(String message){
            out.println(message);
        }

        public void shutdown(){
            try {
                in.close();
                out.close();
                if (!client.isClosed()) {
                    client.close();
                }
            }catch (IOException e){
                // ignore
            }
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.run();
    }
}