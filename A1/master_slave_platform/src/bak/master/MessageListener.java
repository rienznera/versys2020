package bak.master;

import messaging.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class MessageListener implements Runnable {
    private Socket socket;

    @Override
    public void run() {
//run starts messageHandling
        while( true )
        {
            try
            {
                //Init Loop
                System.out.println( "Listening for a connection" );


                // Pass the socket to the RequestHandler thread for processing
                // RequestHandler requestHandler = new RequestHandler( socket );
                //requestHandler.start();
                ois=new ObjectInputStream(socket.getInputStream());
               master.handleMessage((Message) ois.readObject()) ;

            }
            catch (IOException e )
            {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private ObjectInputStream ois;
    private Master master;

    public MessageListener(ObjectInputStream ois, Socket socket, Master master) {
        this.ois = ois;
        this.master = master;
        this.socket = socket;
    }

    public ObjectInputStream getOis() {
        return ois;
    }

    public void setOis(ObjectInputStream ois) {
        this.ois = ois;
    }

    public Master getMaster() {
        return master;
    }

    public void setMaster(Master master) {
        this.master = master;
    }


}
