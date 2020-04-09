package master;

import messaging.MessageType;

import java.net.Socket;

public class Client {
    private MessageType status;
    private int id;
    private Socket socket;

    public Client(MessageType status, int id, Socket socket) {
        this.status = status;
        this.id = id;
        this.socket = socket;
    }

    public MessageType getStatus() {
        return status;
    }

    public void setStatus(MessageType status) {
        this.status = status;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
