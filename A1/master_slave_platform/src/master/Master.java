package master;

import messaging.Message;
import messaging.MessageType;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class Master extends Thread implements Runnable
{
    public static  int  SERVERPORT = 9001;
    private ServerSocket serverSocket;
    private Socket socket;
    private int port;
    private boolean running = false;
    private List<Client> clients;
    private MessageType phase;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;


    public Master( int port )
    {
        this.port = port;
        clients = new ArrayList<>();
        phase = MessageType.INIT;
    }

    public static void main( String[] args )
    {

        System.out.println("Start server on port: " + SERVERPORT );

        Master server = new Master( SERVERPORT );

        server.registerClients();

        // Automatically shutdown in 1 minute
       /* try
        {
            Thread.sleep( 60000 );
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }

        server.stopServer();*/

        System.out.println("Registering finished");

        server.informClients();
    }

    public void registerClients()
    {
        try {
            this.setPhase(MessageType.INIT);
            serverSocket = new ServerSocket(port);
            this.start();
            Thread.sleep(60000);
            this.stopListener();

        }catch (IOException e)
        {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void stopListener() {
        running = false;
        this.interrupt();
    }

    public void informClients(){
        this.setPhase(MessageType.EXE);
       // this.start();
            try {

                for(int i = 0; i< clients.size(); i++){
                    oos = new ObjectOutputStream(clients.get(i).getSocket().getOutputStream());
                    oos.writeObject(new Message(MessageType.EXE, clients.get(i).getId(),0,null ));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }




    public void stopServer()
    {
        running = false;
        this.interrupt();
    }

    @Override
    public void run()
    {
        //run starts messageHandling
        running = true;
        while( running )
        {
            try
            {
                //Init Loop
                System.out.println( "Listening for a connection" );

                // Call accept() to receive the next connection
                socket = serverSocket.accept();

                // Pass the socket to the RequestHandler thread for processing
               // RequestHandler requestHandler = new RequestHandler( socket );
                //requestHandler.start();
                 ois=new ObjectInputStream(socket.getInputStream());
                handleMessage((Message) ois.readObject()) ;

            }
            catch (IOException e )
            {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void handleMessage(Message message){
        switch (this.phase) {
            case INIT:
                if (message.getMessageType() == MessageType.INIT && message.getId() > 0){
                    addClient(message.getId(), socket);
                }
                break;
            case EXE:
                break;
            case RES:
                break;
        }

    }

    public void setPhase(MessageType phase){
        this.phase = phase;
    }

    public void addClient(int id, Socket socket){

            clients.add(new Client(MessageType.INIT, id, socket));
            System.out.println("added client "+ id);
            if (clients.size()>=2){
                stopServer();
            }
    }
}