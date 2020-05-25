package coordSkeleton;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class peer implements Runnable{


	private int id;
	private int port;
	private listenSocket listener; // der Serversocket zum Verbinden der Anderen Peers/Clients/Slaves
	private boolean run = false;
	private ArrayList<Socket> connectedPeers;
	private ArrayList<Socket> connectionToPeers;

	private boolean isCoordinator;
	private int [] peers;
	private long coordinatorTTL; // in Sekunden
	Thread listenerThread;
	public peer(boolean isCoordinator, long coordinatorTTL, int id, int port, int [] peers)
	{
		this.id = id;
		this.port = port;
		run = true;
		this.listener = new listenSocket(this, port);
		this.isCoordinator  = isCoordinator;
		connectedPeers = new ArrayList<Socket>();
		connectionToPeers = new ArrayList<Socket>();
		this.peers = peers;
		System.out.println("Starting listener of peer: " + id);
		listenerThread = new Thread(listener);
		listenerThread.start();
		
	}

	public boolean isActive()
	{
		return run;
	}
	
	/*
	 * Baue das Netzwerk - in unserem Fall ein Mesh
	 */
	public void buildMesh()
	{
		for(int i = 0; i < peers.length;i++)
		{
			if(peers[i] != port)
			{
				try {
					this.debugMessage(" connecting to peer with port: " + peers[i]);
					Socket clS = new Socket("localhost", peers[i]);
					byte [] data = new byte[message.getMessageSize()];
					(new DataInputStream(clS.getInputStream())).read(data);
					message helloMsg = new message(data);
					if(clS.isConnected() && helloMsg.getType() == message.HELLO) 
					{
						debugMessage(" received hello!");
						connectionToPeers.add(clS);
					}
					else
					{
						debugMessage(" could not connect to peer with port: "+ peers[i]);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void run() {
		try
		{
			Thread.sleep(1000);
		}
		catch(InterruptedException ex)
		{
			Thread.currentThread().interrupt();
		}

		// Falls wird der Koordinator sind, simuliere Knotenausfall
		/*	if(isCoordinator)
		{
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				 	public void run() {
				 		stop();
				 	}
			}, coordinatorTTL*1000);
		} */

		while(run)
		{
			// 1. Prüfe periodisch ob der derzeitige Koordinator online ist - wenn wir nicht selbst der Koordinator sind
			// 2. Wenn dieser nicht Online ist initiiere den Bully-Algorithmus und bestimme aus den bestehenden Peers einen neuen Koordinator

			if(isCoordinator)
			{
				for(int i = 0; i < connectedPeers.size(); i++)
				{
					byte [] data = new byte[message.getMessageSize()];
					try {
						DataInputStream is = new DataInputStream(connectedPeers.get(i).getInputStream());
						int bread =is.read(data);
						System.out.println("Coordinator got: " + bread + " bytes.");
						message msg = new message(data);
						debugMessage(" got message with from peer with id " + msg.getPeerId() + " and type " + msg.getType());

						handleMessage(connectedPeers.get(i), msg);

					} catch (IOException e) {
						e.printStackTrace();
					}

				}
			}else
			{
				// if we are not the coordinator send something - its the peer with the highest id/index
				try {	 
					DataOutputStream os = new DataOutputStream(connectionToPeers.get(connectionToPeers.size()-1).getOutputStream());
					message msg = new message(message.HEARTBEAT, id);
					os.write(msg.toByteArray());
					Thread.sleep(1000);
				}catch (InterruptedException e1) {	
					e1.printStackTrace();
				}catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void handleMessage(Socket cl, message msg) // <-- Ausbauen, TODO!
	{
		switch(msg.getType())
		{
		case message.HEARTBEAT:
			DataOutputStream os;
			try {
				os = new DataOutputStream(cl.getOutputStream());
				os.write((new message(message.ALIVE, id)).toByteArray());
				os.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		default:
			debugMessage("unknown message type!");
		}

	}

	public int getId()
	{
		return id;
	}

	public boolean alreadyConnected(int remotePort)
	{

		return false;
	}

	public synchronized void addPeer(Socket clientSocketPeer) {
		debugMessage("Peer " + id + " received connect of peer with IP: " + clientSocketPeer.getInetAddress().toString() + " and port: " + clientSocketPeer.getPort());
		message helloMsg = new message(message.HELLO, id);
		try {
			DataOutputStream os = new DataOutputStream(clientSocketPeer.getOutputStream());
			os.write(helloMsg.toByteArray());
			os.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		connectedPeers.add(clientSocketPeer);
	}

	public void debugMessage(String text)
	{
		System.out.println((isCoordinator ? "Coordinator (peer"+ id +" )" : "Peer " + id + ":") + text);
	}

	public void stop()
	{
		run = false;
		debugMessage("dying.");
		try {

			listener.close();
			for(int i = 0; i < connectedPeers.size(); i++) connectedPeers.get(i).close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
