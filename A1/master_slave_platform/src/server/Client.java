package server;

import messaging.Message;
import messaging.Type;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;


public class Client extends Thread{
    private Socket m_connection;
    private Server server;
    private Integer id = 0;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private Type status;

    public Client(Server server,Socket connection){
        this.server = server;
        m_connection = connection;

        try {
            m_connection.setSoTimeout (server.getTimeout_length());
        } catch (SocketException se) {
            System.err.println ("Unable to set socket option SO_TIMEOUT");
        }

    }
    public Integer getClientId(){
        return this.id;
    }
    public void run() {
        try {
            InputStream in = m_connection.getInputStream();
            DataInputStream dis = new DataInputStream(in);
             ois = new ObjectInputStream(in);
            OutputStream out= m_connection.getOutputStream();
            DataOutputStream dos = new DataOutputStream(out);
             oos = new ObjectOutputStream(out);
            Message received;

            try {
                while (true) {
                    received = (Message) ois.readObject();

                    switch (received.getType()) {
                        case INIT:
                            System.out.println("Init");
                            this.id = received.getId();
                            server.addClient(this);
                            break;

                        case RES:
                            String id = String.valueOf(received.getId());
                            String resultFromSlave = new String(received.getData());
                            System.out.println("slave with id : "+id+" has sent result : "+resultFromSlave);
                            server.getClientResult(this.getClientId(), received);
                            break;

                        default:
                            dos.writeUTF("Invalid input");
                            System.out.println(received.getType());
                            break;
                    }
                }
            }
            catch (InterruptedIOException iioe)
            {
                iioe.printStackTrace();
                System.out.println ("Timeout occurred - killing connection");
                m_connection.close();
                server.removeClient(this);

                System.out.println("dead slave removed");

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        catch (IOException ioe)
        { }

    }

    public Type getStatus() {
        return status;
    }

    public void setStatus(Type status) {
        this.status = status;
    }

    public void doWork(Message message) {
        try {
            oos.writeObject(message);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cleanup() throws IOException {
        this.m_connection.close();
    }
}
