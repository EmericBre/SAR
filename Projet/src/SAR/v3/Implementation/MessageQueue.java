package SAR.v3.Implementation;

public abstract class MessageQueue {

	MessageQueue() {}
	
	interface Listener {
		void received(byte[] msg);
		void sent(byte[] bytes, int offset, int length, Object cookie);
		void closed();
	}
	abstract void setListener(Listener l);
	
	abstract boolean send(byte[] bytes, Object cookie);
	abstract boolean send(byte[] bytes, int offset, int length, Object cookie);
	abstract void close();
	abstract boolean closed();
	
//	abstract void send(byte[] bytes, int offset, int length);
//	
//	abstract byte[] receive();
//	
//	abstract void close();
//	
//	abstract boolean closed();
	 
}
