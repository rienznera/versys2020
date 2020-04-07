package master;

import messaging.MessageType;

public class Client {
    private MessageType status;
    private int id;

    public Client(MessageType status, int id) {
        this.status = status;
        this.id = id;
    }

    public MessageType getStatus() {
        return status;
    }

    public void setStatus(MessageType status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
