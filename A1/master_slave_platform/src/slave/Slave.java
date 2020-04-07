package slave;

import master.Master;
import messaging.Message;
import messaging.MessageType;

import java.io.DataOutputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Slave {
    public static void main(String[] args) {
        try{
            int id = Integer.parseInt(args[0]);
            Socket s=new Socket("localhost", Master.SERVERPORT);
            ObjectOutputStream oout=new ObjectOutputStream(s.getOutputStream());
            oout.writeObject(new Message(MessageType.INIT, id, 0, null));
            oout.flush();
            oout.close();
            s.close();
        }catch(Exception e){System.out.println(e);}
    }
}
