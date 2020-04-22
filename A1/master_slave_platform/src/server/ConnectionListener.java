package server;

import java.io.IOException;
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
                if (server.getNumber_of_connections() > server.getMax_connections() - 1) {

                    System.out.println("Too many Slaves..");


                    active = false;
                } else {
                    Client client;
                    client = new Client(server, connection);
                    client.start();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
