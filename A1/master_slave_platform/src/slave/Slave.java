package slave;

import bak.master.Master;
import messaging.Message;
import messaging.Type;
import server.Server;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Slave {

    public static int id = 0;
    private static int a;
    private static int b;
    public Slave(){};
    public static void main(String[] args) throws IOException {
        try {
            int id = Integer.parseInt(args[0]);
            Socket s=new Socket("localhost", Server.service_port);
           // Slave slave = new Slave (id);
            ObjectOutputStream oout=new ObjectOutputStream(s.getOutputStream());
            //Register Slave
            oout.writeObject(new Message(Type.INIT, id, 0, null));
            oout.flush();
           // ObjectInputStream oin = new ObjectInputStream(s.getInputStream());
           ObjectInputStream oin = new ObjectInputStream(s.getInputStream());
            while (!s.isClosed()) {
                Message received = (Message) oin.readObject();
               // StringTokenizer st = new StringTokenizer(received);
               // type = st.nextToken();
                System.out.println(received);

                switch (received.getType()) {
                    case EXE:
                        System.out.println("got an exercise from master"+ new String(received.getData()));
                        a = Integer.parseInt(new String(received.getData()).split(";")[0]);
                        b = Integer.parseInt((new String(received.getData()).split(";")[1]));
                        int result = a+b;
                        Thread.sleep(3000);
                        //toreturn = "Result "+id+" "+result;
                        //toreturn = "Result "+id+" "+result;
                        oout.writeObject(new Message(Type.RES, id, Integer.toString(result).getBytes().length,Integer.toString(result).getBytes()));

                        System.out.println("result sent");
                        break;

                    default:
                        oout.writeObject(new Message(Type.ERR, id, 0, null));
                        break;
                }
            }

            System.out.println("SLAVE: Socket is closed... finished");
        }catch (EOFException e){
            System.out.println("SLAVE: conenction reset ... finished");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
