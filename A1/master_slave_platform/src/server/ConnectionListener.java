package server;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionListener extends Thread{
    private ServerSocket serverSocket;
    private Server server;
    private boolean active = true;

    public ConnectionListener(ServerSocket serverSocket, Server server) {
        this.serverSocket = serverSocket;
        this.server = server;
    }

    public void run()
    {
        try {
            while (active) {
                System.out.println("Waiting for slaves to connect...");
                Socket connection = serverSocket.accept();

                System.out.println("New slave connected");
                if (server.getNumber_of_connections() > server.max_connections - 1) {
                    // PrintStream pout = new PrintStream (connection.getOutputStream());
                    System.out.println("Too many Slaves..");


                    active = false;
                } else {
                    Client client;
                    client = new Client(server, connection);
                    //clients.adclient);
                    client.start();
                    //slaves.add(connection);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
