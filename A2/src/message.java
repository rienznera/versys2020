package coordSkeleton;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class message {
	
	public static final int ELECTION = 0x01;
	public static final int VICTORY = 0x03;
	public static final int ANSWER = 0x02;
	public static final int HEARTBEAT = 0xFF;
	public static final int ALIVE = 0x0F;
	public static final int HELLO = 0x09;
	
	private int type;	// der typ der nachricht
	private int peerId; // die id des Peers
	
	public message(int type, int peerId)
	{
		this.type = type;
		this.peerId = peerId;
	}
	
	public message(byte [] data)
	{
		this.type = ByteBuffer.wrap(Arrays.copyOfRange(data, 0, 4)).getInt();
		this.peerId = ByteBuffer.wrap(Arrays.copyOfRange(data, 4, 8)).getInt();
	}
		
	public byte [] toByteArray()
	{
		ByteBuffer b =  ByteBuffer.allocate(getMessageSize());
		b.putInt(this.type);
		b.putInt(this.peerId);
		return b.array();
	}
	
	public static final int getMessageSize()
	{
		return 2*Integer.BYTES;
	}
	
	public int getType()
	{
		return type;
	}
	
	public int getPeerId()
	{
		return peerId;
	}
}
