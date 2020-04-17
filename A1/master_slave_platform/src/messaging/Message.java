package messaging;

import java.io.Serializable;
import java.util.Arrays;

public class Message implements Serializable {
    private Type type;
    private int id;
    private int length;
    private byte[] data;

    public Message(Type type, int id, int length, byte[] data) {
        this.type = type;
        this.id = id;
        this.length = length;
        this.data = data;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
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
                "type=" + type +
                ", id=" + id +
                ", length=" + length +
                ", data=" + Arrays.toString(data) +
                '}';
    }
}
