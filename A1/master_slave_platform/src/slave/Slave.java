package slave;

import master.Master;
import messaging.Message;
import messaging.MessageType;

import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Slave {
    private boolean active;
    private int id;

    public Slave(int id) {
        this.id = id;
        active = true;
    }

    public void processMessage(Message message){
        if (this.id == message.getId()){

        }
    }

    public int getId() {
        return id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setId(int id) {
        this.id = id;
    }



    public static void main(String[] args) {
        try{
            int id = Integer.parseInt(args[0]);
            Socket s=new Socket("localhost", Master.SERVERPORT);
            Slave slave = new Slave (id);
            ObjectOutputStream oout=new ObjectOutputStream(s.getOutputStream());
            //Register Slave
            oout.writeObject(new Message(MessageType.INIT, slave.getId(), 0, null));
            oout.flush();
            //oout.close();
            ObjectInputStream oin = new ObjectInputStream(s.getInputStream());

            while(slave.isActive()){

              Message response =  slave.handleMessage((Message) oin.readObject());
               if (response!= null){
                   oout.writeObject(response);
               }
            }

            oout.close();
            s.close();
        }catch(Exception e){e.printStackTrace();}
    }

    private Message handleMessage(Message message) {
        Message ret = null;
        System.out.println("handle: "+ message.toString());
    switch (message.getMessageType()){
        case EXE:
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ret = message;
            message.setMessageType( MessageType.RES);
            byte[] help = "This is a executed message from slave".getBytes();
            message.setLength(help.length);
            message.setData(help);
            break;
    }
        return null;
    }
}
