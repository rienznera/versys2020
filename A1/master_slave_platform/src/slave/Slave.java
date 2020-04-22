package slave;


import messaging.Message;
import messaging.Type;
import server.Server;
import java.io.*;
import java.net.Socket;


public class Slave {

    public static int id = 0;
    private static int a;
    private static int b;
    public Slave(){};
    public static void main(String[] args) throws IOException {
        if(args.length < 2) {
            System.out.println("ERROR!!! no correct start parameters!");
        } else {
            try {
                int id = Integer.parseInt(args[0]);
                Boolean execute = Boolean.parseBoolean(args[1]);
                Socket s=new Socket("localhost", Server.service_port);
                System.out.println("Client with id:" + id + " is connected = " + s.isConnected());
                ObjectOutputStream oout=new ObjectOutputStream(s.getOutputStream());
                //Register Slave
                oout.writeObject(new Message(Type.INIT, id, 0, null));
                oout.flush();

                ObjectInputStream oin = new ObjectInputStream(s.getInputStream());
                while (!s.isClosed()) {
                    Message received = (Message) oin.readObject();

                    System.out.println(received);

                    switch (received.getType()) {
                        case EXE:
                            System.out.println("got an exercise from master"+ new String(received.getData()));
                            a = Integer.parseInt(new String(received.getData()).split(";")[0]);
                            b = Integer.parseInt((new String(received.getData()).split(";")[1]));
                            int result = a*b;
                            Thread.sleep(3000);

                            if(execute) {
                                oout.writeObject(new Message(Type.RES, id, Integer.toString(result).getBytes().length, Integer.toString(result).getBytes()));
                            }
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
}
