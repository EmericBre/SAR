package SAR.v3.Implementation;

public abstract class MessageQueue {

	MessageQueue() {}
	
	public interface Listener {
		void received(byte[] msg);
		void sent(byte[] bytes, int offset, int length);
		void closed();
	}
	public abstract void setListener(Listener l);
	
	public abstract boolean send(byte[] bytes);
	public abstract boolean send(byte[] bytes, int offset, int length);
	public abstract void close();
	public abstract boolean closed();
	 
}
