package bak.master;

import messaging.Type;

import java.net.Socket;

public class Client {
    private Type status;
    private int id;
    private Socket socket;

    public Client(Type status, int id, Socket socket) {
        this.status = status;
        this.id = id;
        this.socket = socket;
    }

    public Type getStatus() {
        return status;
    }

    public void setStatus(Type status) {
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
