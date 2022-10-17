package SAR.v3.Implementation;

public abstract class QueueBroker {
	
	 QueueBroker(String name) {}
	 
	 interface AcceptListener {
		 void accepted(int port, MessageQueue queue);
	 }
	 
	 abstract boolean bind(int port, AcceptListener listener);
	 abstract boolean unbind(int port);
	 
	 interface ConnectListener {
		 void connected(String name, int port, MessageQueue queue);
		 void refused(String name, int port);
	 }
	 
	 abstract boolean connect(String name, int port, ConnectListener listener);
	 
//	 abstract MessageQueue accept(int port);
//	 
//	 abstract MessageQueue connect(String name, int port);

}
