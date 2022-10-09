package SAR.v2.Implementation;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.locks.ReentrantLock;

public class MessageQueueImpl extends MessageQueue {
	
	private boolean closed;
	private ChannelImpl channel;
	private ReentrantLock sendlock;
	private ReentrantLock receivelock;

	MessageQueueImpl(ChannelImpl channel) {
		super();
		this.closed = false;
		this.channel = channel;
		this.receivelock = new ReentrantLock();
		this.sendlock = new ReentrantLock();
	}
	
	void send(byte[] bytes, int offset, int length) {
		this.sendlock.lock();
		byte[] messageLength = ByteBuffer.allocate(Integer.BYTES).putInt(length).array();
		int counter = 0;
		try {
			counter = this.channel.write(messageLength, 0, Integer.BYTES);
			counter += this.channel.write(bytes, 0, length);
			if (counter != length+Integer.BYTES) {
				return;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.sendlock.unlock();
	}
	
	byte[] receive() {
		this.receivelock.lock();
		byte[] bytes = new byte[64];
		
		try {
			int result = this.channel.read(bytes, 0, Integer.BYTES);
			if (result != Integer.BYTES) {
				throw new IOException("Impossible to read message length");
			}
			int length= (bytes[0]<<24)&0xff000000|
				       (bytes[1]<<16)&0x00ff0000|
				       (bytes[2]<< 8)&0x0000ff00|
				       (bytes[3]<< 0)&0x000000ff;
			result = this.channel.read(bytes, 4, length);
			this.receivelock.unlock();
			return bytes;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			this.receivelock.unlock();
			e.printStackTrace();
			return null;
		}
	}
	
	void close() {
		this.closed = true;
		this.channel.disconnect();
	}
	
	boolean closed() {
		return this.closed;
	}
	
	boolean getChannelConnect() {
		return this.channel.disconnected();
	}
	
}
