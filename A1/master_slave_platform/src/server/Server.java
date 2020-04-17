package server;

import messaging.Message;
import messaging.Type;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

    public class Server  {

        private Socket m_connection;
        private static int number_of_connections = 0;
        private static final int max_connections = 2;
        public static final int service_port = 9056;
        public boolean finished = false;
        private static ArrayList<Socket> slaves = new ArrayList<Socket>();
        public static final int timeout_length = 10000;
        private static HashMap<Integer, Client> clients = new HashMap<>();
        public Server(){}
        public Server (Socket connection) {
            m_connection = connection;

            try {
                m_connection.setSoTimeout (timeout_length);
            } catch (SocketException se) {
                System.err.println ("Unable to set socket option SO_TIMEOUT");
            }
            number_of_connections++;
        }

        public static void addClient(Client client) {
            clients.put(client.getClientId(), client);
            number_of_connections++;
        }



        public static void main(String args[]) throws Exception
        {
            ServerSocket serverSocket = new ServerSocket (service_port);
            Server server = new Server();
            //Connecting slaves
            boolean active = true;
            while (active) {
                System.out.println("Waiting for slaves to connect...");
                Socket connection = serverSocket.accept();
                System.out.println("New slave connected");
                if (number_of_connections > max_connections-1) {
                    PrintStream pout = new PrintStream (connection.getOutputStream());
                    pout.println ("Too many Slaves..");
                    connection.close();
                    active = false;
                }
                else {
                    Client client;
                    client = new Client(server, connection);
                    //clients.adclient);
                    client.start();
                    //slaves.add(connection);
                }
            }

            //Send Work to Clients
          server.sendExerciseToSlaves(10, 20);

            while (!server.getFinished()){
                Thread.sleep(1000);
            }

        }

        private boolean getFinished() {
            return this.finished;
        }

        public void sendExerciseToSlaves(int a, int b){
            for (Client c:clients.values()
            ) {
                String work = "1;2";
                c.doWork(new Message(Type.EXE,0, work.getBytes().length, work.getBytes()));
                clients.get(c.getClientId()).setStatus(Type.EXE);
            }
        }



        public void removeClient(Client client) {
            clients.remove(client.getClientId());
        }

        public synchronized void getClientResult(Integer clientId, Message received) {
            System.out.println("SERVER: got result from " + clientId);
            clients.get(clientId).setStatus(Type.RES);
            boolean check = true;
            for (Client c:clients.values()
                 ){
                if (c.getStatus() != Type.RES){
                    check = false;
                }
            }
            if (check){
                this.finished = true;
                System.out.println("SERVER: work is done !!");
            }

        }
    }
