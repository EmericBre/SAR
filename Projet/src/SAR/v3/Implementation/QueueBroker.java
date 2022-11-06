package SAR.v3.Implementation;

public abstract class QueueBroker {
	
	 public String name;
	
	 public QueueBroker(String name) {
		 this.name = name;
	 }
	 
	 public interface AcceptListener {
		 void accepted(int port, MessageQueue queue);
	 }
	 
	 public abstract boolean bind(int port, AcceptListener listener);
	 public abstract boolean unbind(int port);
	 
	 public interface ConnectListener {
		 void connected(String name, int port, MessageQueue queue);
		 void refused(String name, int port);
	 }
	 
	 public abstract boolean connect(String name, int port, ConnectListener listener);
	 
}
