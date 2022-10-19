package SAR.v2.Implementation;

public abstract class MessageQueue {

	MessageQueue() {}
	
	abstract void send(byte[] bytes, int offset, int length);
	
	abstract byte[] receive() throws InterruptedException;
	
	abstract void close();
	
	abstract boolean closed();
	 
}
