package master;

import messaging.Message;
import messaging.MessageType;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;

public class Master extends Thread
{
    public static  int  SERVERPORT = 9001;
    private ServerSocket serverSocket;
    private int port;
    private boolean running = false;
    private HashSet<Integer> clients;
    private MessageType phase;

    public Master( int port )
    {
        this.port = port;
        clients = new HashSet<Integer>();
        phase = MessageType.INIT;
    }

    public static void main( String[] args )
    {


            System.out.println("Start server on port: " + SERVERPORT );

        Master server = new Master( SERVERPORT );
        server.startServer();

        // Automatically shutdown in 1 minute
        try
        {
            Thread.sleep( 60000 );
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }

        server.stopServer();
    }

    public void startServer()
    {
        try
        {
            serverSocket = new ServerSocket( port );
            this.start();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void stopServer()
    {
        running = false;
        this.interrupt();
    }

    @Override
    public void registerClients()
    {
        running = true;
        while( running )
        {
            try
            {
                //Init Loop
                System.out.println( "Listening for a connection" );

                // Call accept() to receive the next connection
                Socket socket = serverSocket.accept();

                // Pass the socket to the RequestHandler thread for processing
               // RequestHandler requestHandler = new RequestHandler( socket );
                //requestHandler.start();
                ObjectInputStream ois=new ObjectInputStream(socket.getInputStream());
                Message message = (Message) ois.readObject() ;
                if (message.getMessageType() == MessageType.INIT && message.getId() > 0){
                    addClient(message.getId());
                }

            }
            catch (IOException e )
            {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleMessage(Message message){
        switch (this.phase) {
            case INIT:

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

    public void addClient(int id){
        System.out.println("added client: "+id);
            clients.add(id);
            if (clients.size()>=2){
                stopServer();
            }
    }
}