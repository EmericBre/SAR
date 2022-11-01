package SAR.v2.Implementation;

public abstract class MessageQueue {

	MessageQueue() {}
	
	public abstract void send(byte[] bytes, int offset, int length);
	
	public abstract byte[] receive() throws InterruptedException;
	
	public abstract void close();
	
	public abstract boolean closed();
	 
}
