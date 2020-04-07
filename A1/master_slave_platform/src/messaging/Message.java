package messaging;

import java.io.Serializable;
import java.util.Arrays;

public class Message implements Serializable {
    private MessageType messageType;
    private int id;
    private int length;
    private byte[] data;

    public Message(MessageType messageType, int id, int length, byte[] data) {
        this.messageType = messageType;
        this.id = id;
        this.length = length;
        this.data = data;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageType=" + messageType +
                ", id=" + id +
                ", length=" + length +
                ", data=" + Arrays.toString(data) +
                '}';
    }
}
